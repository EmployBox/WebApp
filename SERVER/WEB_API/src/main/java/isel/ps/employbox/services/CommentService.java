package isel.ps.employbox.services;

import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.model.entities.Comment;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CommentService {
    private final RapperRepository<Comment,Long> commentRepo;

    public CommentService(RapperRepository<Comment, Long> commentRepo) {
        this.commentRepo = commentRepo;
    }


    public Stream<Comment> getComments(long accountId, String type){
        return StreamSupport.stream(commentRepo.findAll().spliterator(), false)
                .filter(curr-> type.equals("done") && curr.getAccountIdFrom() == accountId || type.equals("received")&& curr.getAccountIdTo()== accountId );
    }

    public Comment getComment(long accountFrom, long accountTo){
        return null;
    }

    public void updateComment(Comment comment) {

    }

    public void createComment(Comment comment) {

    }

    public void deleteComment(long id, long accountTo) {

    }
}
