package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Comment;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.OutComment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class CommentBinder implements ModelBinder<Comment,OutComment,InComment> {


    @Override
    public Mono<OutComment> bindOutput(CompletableFuture<Comment> commentCompletableFuture) {
        return Mono.fromFuture(
                commentCompletableFuture.thenApply(
                        comment -> new OutComment(
                                comment.getAccountIdFrom(),
                                comment.getAccountIdTo(),
                                comment.getIdentityKey(),
                                comment.getMainCommendID(),
                                comment.getDate().toString(),
                                comment.getText())
                )
        );
    }

    @Override
    public Comment bindInput(InComment object) {
        return null;
    }
}
