package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CommentBinder;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutComment;
import isel.ps.employbox.services.CommentService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

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
    public Resource<HalCollection> getAllComments(@PathVariable long accId, @RequestParam String type){
        if(type.equals("done") || type.equals("received"))
            return commentBinder.bindOutput(
                    commentService.getComments(accId, type),
                    this.getClass(),
                    accId
            );
        else
            throw new BadRequestException("Type must be either \"done\" or \"received\"");
    }

    @GetMapping("{commentId}")
    public Resource<OutComment> getComment(@PathVariable long accountId, @RequestBody long accountTo, @PathVariable long commentId){
        return commentBinder.bindOutput(commentService.getComment(accountId, accountTo, commentId));
    }

    @PutMapping
    public void updateComment(@PathVariable long id, @RequestParam long accountTo, @RequestBody InComment comment){
        if(id != comment.getAccountIdFrom() || accountTo != comment.getAccountIdTo()) throw new BadRequestException(badRequest_IdsMismatch);
        commentService.updateComment(commentBinder.bindInput(comment));
    }

    @PostMapping
    public void createComment(@PathVariable long accountFromId, @RequestParam long accountTo, @RequestBody InComment comment){
        if(accountFromId != comment.getAccountIdFrom() || accountTo != comment.getAccountIdTo()) throw new BadRequestException(badRequest_IdsMismatch);
        commentService.createComment(commentBinder.bindInput(comment));
    }

    @DeleteMapping("{commentId}")
    public void deleteComment(@PathVariable long accountFromId, @PathVariable long commentId){
        commentService.deleteComment(accountFromId, commentId);
    }
}
