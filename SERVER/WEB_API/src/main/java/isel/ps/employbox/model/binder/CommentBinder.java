package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Comment;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.OutComment;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class CommentBinder extends ModelBinder<Comment, OutComment, InComment> {

    @Override
    public Stream<Comment> bindInput(Stream<InComment> list) {
        return null;
    }

    @Override
    public Resource<OutComment> bindOutput(Comment object) {
        return null;
    }

    @Override
    public Comment bindInput(InComment object) {
        return null;
    }
}
