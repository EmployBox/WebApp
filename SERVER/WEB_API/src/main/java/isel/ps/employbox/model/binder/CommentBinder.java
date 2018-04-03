package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.ModelBinder;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.OutComment;
import isel.ps.employbox.model.entities.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentBinder implements ModelBinder<Comment, OutComment, InComment, Long> {
    @Override
    public List<OutComment> bindOutput(List<Comment> list) {
        return null;
    }

    @Override
    public List<Comment> bindInput(List<InComment> list) {
        return null;
    }

    @Override
    public OutComment bindOutput(Comment object) {
        return null;
    }

    @Override
    public Comment bindInput(InComment object) {
        return null;
    }
}
