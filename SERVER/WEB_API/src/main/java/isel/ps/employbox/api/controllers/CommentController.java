package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.exceptions.BadRequestException;
import isel.ps.employbox.api.model.ModelBinder;
import isel.ps.employbox.api.model.input.InComment;
import isel.ps.employbox.api.model.output.OutComment;
import isel.ps.employbox.api.services.CommentService;
import isel.ps.employbox.dal.model.Comment;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static isel.ps.employbox.api.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/{id}/comments")
public class CommentController {
    private final CommentService commentService;
    private final ModelBinder<Comment, OutComment, InComment, Long> commentBinder;

    public CommentController(CommentService commentService, ModelBinder<Comment, OutComment, InComment, Long> commentBinder) {
        this.commentService = commentService;
        this.commentBinder = commentBinder;
    }

    @GetMapping
    public List<OutComment> getAllComments(@PathVariable long id, @RequestParam String type){
        if(type.equals("done") || type.equals("received"))
            return commentBinder.bindOutput(commentService.getComments(id, type));
        else
            throw new BadRequestException("Type must be either \"done\" or \"received\"");
    }

    @GetMapping
    public Optional<OutComment> getComment(@PathVariable long id, @RequestParam long accountTo){
        return commentService.getComment(id, accountTo)
                .map(commentBinder::bindOutput);
    }

    @PutMapping
    public void updateComment(@PathVariable long id, @RequestParam long accountTo, @RequestBody InComment comment){
        if(id != comment.getAccountIdFrom() || accountTo != comment.getAccountIdTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        commentService.updateComment(commentBinder.bindInput(comment));
    }

    @PostMapping
    public void createComment(@PathVariable long id, @RequestParam long accountTo, @RequestBody InComment comment){
        if(id != comment.getAccountIdFrom() || accountTo != comment.getAccountIdTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        commentService.createComment(commentBinder.bindInput(comment));
    }

    @DeleteMapping
    public void deleteComment(@PathVariable long id, @RequestParam long accountTo){
        commentService.deleteComment(id, accountTo);
    }
}
