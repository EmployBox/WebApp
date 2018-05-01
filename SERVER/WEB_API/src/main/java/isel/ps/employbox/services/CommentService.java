package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Comment;
import javafx.util.Pair;
import org.github.isel.rapper.DataRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class CommentService {
    private final DataRepository<Account, Long> accountRepo;
    private final DataRepository<Comment, Long> commentRepo;
    private final AccountService accountService;

    public CommentService(DataRepository<Account, Long> accountRepo, DataRepository<Comment, Long> commentRepo, AccountService accountService) {
        this.accountRepo = accountRepo;
        this.commentRepo = commentRepo;
        this.accountService = accountService;
    }


    public CompletableFuture<Stream<Comment>> getComments(long accountFromId, String type) {
        return commentRepo.findAll().thenApply(
                list -> list
                        .stream()
                        .filter(curr -> type.equals("done") && curr.getAccountIdFrom() == accountFromId || type.equals("received") && curr.getAccountIdTo() == accountFromId));
    }

    public CompletableFuture<Comment> getComment(long accountFromId, long accountToId, long commentId, String email) {
        return accountService.getAccount(accountFromId, email)
                .thenCompose(
                        __ -> commentRepo.findById(commentId)
                                .thenApply(ocomment -> {
                                            Comment comment = ocomment.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.resourceNotfound_comment));
                                            if (comment.getAccountIdFrom() != accountFromId && comment.getAccountIdTo() != accountToId)
                                                throw new BadRequestException(ErrorMessages.badRequest_IdsMismatch);
                                            return comment;
                                        }
                                ));
    }

    public Mono<Void> updateComment(Comment comment, String username) {
        return Mono.fromFuture(
                getComment(comment.getAccountIdFrom(), comment.getAccountIdTo(), comment.getIdentityKey(), username)
                        .thenCompose(__ -> commentRepo.update(comment))
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                        })
        );
    }

    public CompletableFuture<Comment> createComment(Comment comment, String email) {
        return CompletableFuture.allOf(
                accountService.getAccount(comment.getAccountIdFrom(), email),
                accountService.getAccount(comment.getAccountIdTo())
        ).thenCompose(__ -> commentRepo.create(comment)
        ).thenApply(res -> {
            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
            return comment;
        });
    }

    public Mono<Void> deleteComment(long commentId, String email) {
        return Mono.fromFuture(
                commentRepo.findById(commentId)
                        .thenAccept(ocomment -> {
                            Comment comment = ocomment.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.resourceNotfound_comment));
                            accountRepo.findWhere(new Pair<>("email", email))
                                    .thenCompose(list -> {
                                        if (comment.getAccountIdFrom() != list.get(0).getIdentityKey())
                                            throw new UnauthorizedException(ErrorMessages.unAuthorized);
                                        return commentRepo.deleteById(commentId);
                                    }).thenAccept(res -> {
                                if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                            });
                        })
        );
    }
}
