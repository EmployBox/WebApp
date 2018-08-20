package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Comment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.getCollectionPageFuture;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class CommentService {
    private final AccountService accountService;

    public CommentService( AccountService accountService) {
        this.accountService = accountService;
    }

    public CompletableFuture<CollectionPage<Comment>> getComments(long accountId, int page, int pageSize, String orderColumn, String orderClause) {
        ArrayList<Condition> conditions = new ArrayList<>();
        ServiceUtils.evaluateOrderClause(orderColumn,orderClause, conditions);
        conditions.add(new EqualAndCondition<>("accountIdDest", accountId));


        return accountService.getAccount(accountId)
                .thenCompose(__-> ServiceUtils.getCollectionPageFuture(Comment.class, page, pageSize, conditions.toArray(new Condition[conditions.size()])));
    }

    public CompletableFuture<Comment> getComment(long accountIdTo, long commentId) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Account, Long> accoutnMapper = getMapper(Account.class, unitOfWork);
        DataMapper<Comment, Long> commentMapper = getMapper(Comment.class, unitOfWork);


        CompletableFuture<Comment> future = accoutnMapper.findById(accountIdTo)
                .thenCompose(account -> commentMapper.findById(commentId))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(ocomment -> {
                            Comment comment = ocomment.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMMENT));
                            if (comment.getAccountIdFrom() != accountIdTo)
                                throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                            return comment;
                        }
                );
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateComment(Comment comment, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Comment, Long> commentMapper = getMapper(Comment.class, unitOfWork);

        CompletableFuture<Void> future = accountService.getAccount(comment.getAccountIdFrom(), email)
                .thenCompose(acc ->
                        commentMapper.find(
                                new EqualAndCondition<>("commentId", comment.getIdentityKey()),
                                new EqualAndCondition<>("accountIdFrom", comment.getAccountIdFrom()),
                                new EqualAndCondition<>("accountIdDest", comment.getAccountIdDest())
                        )
                ).thenCompose(commentRes -> {
                    if (commentRes.size() == 0)
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMMENT);
                    return commentMapper.update(comment);
                }).thenCompose(res -> unitOfWork.commit());

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
                    return accountMapper.find(new EqualAndCondition<>("email", String.valueOf(email)))
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

    public CompletableFuture<CollectionPage<Comment>> getCommentReplies(long accountId, long commentId) {
        UnitOfWork unitOfWork = new UnitOfWork();

        return getComment(accountId,commentId)
                .thenCompose(comment -> comment.getReplies().apply(unitOfWork))
                .thenCompose( replies -> getCollectionPageFuture(Comment.class, 0, replies.size()));
    }
}
