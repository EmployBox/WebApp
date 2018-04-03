package isel.ps.employbox.services;

import isel.ps.employbox.model.entities.Comment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {


    public List<Comment> getComments(long id, String type){
        return null;
    }

    public Optional<Comment> getComment(long id, long accountTo){
        return Optional.empty();
    }

    public void updateComment(Comment comment) {

    }

    public void createComment(Comment comment) {

    }

    public void deleteComment(long id, long accountTo) {

    }
}
