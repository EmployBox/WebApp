package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Comment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<CollectionPage<Comment>> getComments(long accountFromId, int page, int pageSize) {
        return accountService.getAccount(accountFromId)
                .thenCompose(__-> ServiceUtils.getCollectionPageFuture(commentRepo, page, pageSize, new Pair<>("accountIdFrom", accountFromId)));
    }

    public CompletableFuture<Comment> getComment(long accountFromId, long accountToId, long commentId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return accountService.getAccount(accountFromId, email)
                .thenCompose(account -> commentRepo.findById(unitOfWork, commentId))
                .thenCompose( res -> unitOfWork.commit().thenApply( aVoid -> res))
                .thenApply(ocomment -> {
                            Comment comment = ocomment.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMMENT));
                            if (comment.getAccountIdFrom() != accountFromId && comment.getAccountIdDest() != accountToId)
                                throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                            return comment;
                        }
                );
    }

    public Mono<Void> updateComment(Comment comment, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return Mono.fromFuture(
                getComment(comment.getAccountIdFrom(), comment.getAccountIdDest(), comment.getIdentityKey(), username)
                        .thenCompose(__ -> commentRepo.update(unitOfWork, comment))
                        .thenCompose( res -> unitOfWork.commit())
        );
    }

    public CompletableFuture<Comment> createComment(Comment comment, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return CompletableFuture.allOf(
                accountService.getAccount(comment.getAccountIdFrom(), email),
                accountService.getAccount(comment.getAccountIdDest())
        )
                .thenCompose(aVoid -> commentRepo.create(unitOfWork, comment))
                .thenCompose( res -> unitOfWork.commit().thenApply( aVoid -> comment));
    }

    public Mono<Void> deleteComment(long commentId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return Mono.fromFuture(
                commentRepo.findById(unitOfWork, commentId)
                        .thenCompose(ocomment -> {
                            Comment comment = ocomment.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMMENT));
                            return accountRepo.findWhere(unitOfWork, new Pair<>("email", email))
                                    .thenCompose(list -> {
                                        if (comment.getAccountIdFrom() != list.get(0).getIdentityKey())
                                            throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                                        return commentRepo.deleteById(unitOfWork, commentId);
                                    });
                        }).thenCompose(aVoid -> unitOfWork.commit())
        );
    }
}
