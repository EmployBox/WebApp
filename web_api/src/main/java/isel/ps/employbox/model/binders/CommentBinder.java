package isel.ps.employbox.model.binders;

import isel.ps.employbox.model.entities.Comment;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.OutComment;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CommentBinder implements ModelBinder<Comment,OutComment,InComment> {

    @Override
    public CompletableFuture<OutComment> bindOutput(Comment comment) {
        return CompletableFuture.completedFuture(
                new OutComment(
                        comment.getAccountIdFrom(),
                        comment.getAccountIdDest(),
                        comment.getIdentityKey(),
                        comment.getMainCommendID(),
                        comment.getDatetime(),
                        comment.getText()
                ));
    }

    @Override
    public Comment bindInput(InComment comment) {
        return new Comment(
                        comment.getCommmentId(),
                        comment.getAccountIdFrom(),
                        comment.getAccountIdTo(),
                        comment.getMainCommentId(),
                        comment.getDatetime(),
                        comment.getText(),
                        comment.getStatus(),
                        comment.getVersion()
                );
    }
}
