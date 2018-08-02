package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import com.github.jayield.rapper.utils.Pair;
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

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class CommentService {
    private final AccountService accountService;

    public CommentService( AccountService accountService) {
        this.accountService = accountService;
    }

    public CompletableFuture<CollectionPage<Comment>> getComments(long accountFromId, int page, int pageSize) {
        return accountService.getAccount(accountFromId)
                .thenCompose(__-> ServiceUtils.getCollectionPageFuture(Comment.class, page, pageSize, new Pair<>("accountIdFrom", accountFromId)));
    }

    public CompletableFuture<Comment> getComment(long accountFromId, long accountToId, long commentId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Comment, Long> commentMapper = getMapper(Comment.class, unitOfWork);
        CompletableFuture<Comment> future = accountService.getAccount(accountFromId, email)
                .thenCompose(account -> commentMapper.findById(commentId))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(ocomment -> {
                            Comment comment = ocomment.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMMENT));
                            if (comment.getAccountIdFrom() != accountFromId && comment.getAccountIdDest() != accountToId)
                                throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                            return comment;
                        }
                );
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateComment(Comment comment, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Comment, Long> commentMapper = getMapper(Comment.class, unitOfWork);
        CompletableFuture<Void> future = getComment(comment.getAccountIdFrom(), comment.getAccountIdDest(), comment.getIdentityKey(), username)
                .thenCompose(__ -> commentMapper.update( comment))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public CompletableFuture<Comment> createComment(Comment comment, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Comment, Long> commentMapper = getMapper(Comment.class, unitOfWork);
        CompletableFuture<Comment> future = CompletableFuture.allOf(
                accountService.getAccount(comment.getAccountIdFrom(), email),
                accountService.getAccount(comment.getAccountIdDest())
        )
                .thenCompose(aVoid -> commentMapper.create(comment))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> comment));
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> deleteComment(long commentId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Comment, Long> commentMapper = getMapper(Comment.class, unitOfWork);
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);
        CompletableFuture<Void> future = commentMapper.findById( commentId)
                .thenCompose(ocomment -> {
                    Comment comment = ocomment.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMMENT));
                    return accountMapper.findWhere(new Pair<>("email", email))
                            .thenCompose(list -> {
                                if (comment.getAccountIdFrom() != list.get(0).getIdentityKey())
                                    throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                                return commentMapper.deleteById(commentId);
                            });
                }).thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
