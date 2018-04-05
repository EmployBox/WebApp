package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CommentBinder;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutComment;
import isel.ps.employbox.services.CommentService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/{id}/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentBinder commentBinder;

    public CommentController(CommentService commentService, CommentBinder commentBinder) {
        this.commentService = commentService;
        this.commentBinder = commentBinder;
    }

    @GetMapping
    public Resource<HalCollection> getAllComments(@PathVariable long id, @RequestParam String type){
        if(type.equals("done") || type.equals("received"))
            return commentBinder.bindOutput(
                    commentService.getComments(id, type),
                    this.getClass(),
                    id
            );
        else
            throw new BadRequestException("Type must be either \"done\" or \"received\"");
    }

    @GetMapping
    public Resource<OutComment> getComment(@PathVariable long id, @RequestParam long accountTo){
        return commentBinder.bindOutput(commentService.getComment(id, accountTo));
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
