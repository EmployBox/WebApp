package isel.ps.employbox.services;

import isel.ps.employbox.model.entities.Comment;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class CommentService {


    public Stream<Comment> getComments(long id, String type){
        return null;
    }

    public Comment getComment(long id, long accountTo){
        return null;
    }

    public void updateComment(Comment comment) {

    }

    public void createComment(Comment comment) {

    }

    public void deleteComment(long id, long accountTo) {

    }
}
