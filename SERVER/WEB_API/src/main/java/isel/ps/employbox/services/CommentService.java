package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Comment;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CommentService {
    private final RapperRepository<Comment,Long> commentRepo;
    private final AccountService accountService;

    public CommentService(RapperRepository<Comment, Long> commentRepo, AccountService accountService) {
        this.commentRepo = commentRepo;
        this.accountService = accountService;
    }


    public Stream<Comment> getComments(long accountFromId, String type){
        return StreamSupport.stream(commentRepo.findAll().spliterator(), false)
                .filter(curr-> type.equals("done") && curr.getAccountIdFrom() == accountFromId || type.equals("received")&& curr.getAccountIdTo()== accountFromId );
    }

    public Comment getComment(long accountFromId, long accountToId, long commentId){
        Optional<Comment> ocomment = commentRepo.findById(commentId);
        if(!ocomment.isPresent())
            throw new ResourceNotFoundException( ErrorMessages.resourceNotfound_comment);

        Comment comment = ocomment.get();
        if(comment.getAccountIdFrom() != accountFromId && comment.getAccountIdTo() != accountToId)
            throw new BadRequestException(ErrorMessages.badRequest_IdsMismatch);

        return comment;
    }

    public void updateComment(Comment comment) {
        getComment(comment.getAccountIdFrom(), comment.getAccountIdTo(), comment.getCommentID());
        commentRepo.update(comment);
    }

    public void createComment(Comment comment) {
        accountService.getAccount(comment.getAccountIdFrom());
        accountService.getAccount(comment.getAccountIdTo());

        commentRepo.create(comment);
    }

    public void deleteComment( long commentId) {
        commentRepo.deleteById(commentId);
    }
}
