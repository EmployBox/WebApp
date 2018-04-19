package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Comment;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.OutComment;
import org.springframework.stereotype.Component;

@Component
public class CommentBinder extends ModelBinder<Comment, OutComment, InComment> {


    @Override
    public OutComment bindOutput(Comment object) {
        return new OutComment(
                object.getAccountIdFrom(),
                object.getAccountIdTo(),
                object.getIdentityKey(),
                object.getMainCommendID(),
                object.getDate().toString(),
                object.getText()
        );
    }

    @Override
    public Comment bindInput(InComment object) {
        return null;
    }
}
