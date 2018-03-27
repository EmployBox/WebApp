package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.ModelBinder;
import isel.ps.employbox.api.model.input.InComment;
import isel.ps.employbox.api.model.output.OutComment;
import isel.ps.employbox.dal.model.Comment;
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
