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
    Stream<Comment> bindInput(Stream<InComment> list) {
        return null;
    }

    @Override
    Resource<OutComment> bindOutput(Comment object) {
        return null;
    }

    @Override
    Comment bindInput(InComment object) {
        return null;
    }
}
