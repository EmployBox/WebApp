package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CommentBinder;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutComment;
import isel.ps.employbox.services.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<HalCollection> getAllComments(@PathVariable long accId, @RequestParam String type){
        if(type.equals("done") || type.equals("received"))
            return commentBinder.bindOutput(
                    commentService.getComments(accId, type),
                    this.getClass()
            );
        else
            throw new BadRequestException("Type must be either \"done\" or \"received\"");
    }

    @GetMapping("{commentId}")
    public Mono<OutComment> getComment(@PathVariable long accountId, @RequestBody long accountTo, @PathVariable long commentId, Authentication authentication){
        return commentBinder.bindOutput(
                commentService.getComment(accountId, accountTo, commentId,  authentication.getName())
        );
    }

    @PutMapping
    public Mono<Void> updateComment(
            @PathVariable long id,
            @RequestParam long accountTo,
            @RequestBody InComment comment,
            Authentication authentication
    ){
        if(id != comment.getAccountIdFrom() || accountTo != comment.getAccountIdTo()) throw new BadRequestException(badRequest_IdsMismatch);
        return commentService.updateComment(commentBinder.bindInput(comment), authentication.getName());
    }

    @PostMapping
    public Mono<OutComment> createComment(
            @PathVariable long accountFromId,
            @RequestParam long accountTo,
            @RequestBody InComment comment,
            Authentication authentication
    ){
        if(accountFromId != comment.getAccountIdFrom() || accountTo != comment.getAccountIdTo()) throw new BadRequestException(badRequest_IdsMismatch);
        return commentBinder.bindOutput( commentService.createComment(commentBinder.bindInput(comment), authentication.getName()));
    }

    @DeleteMapping("{commentId}")
    public void deleteComment( @PathVariable long commentId, Authentication authentication){
        commentService.deleteComment( commentId,  authentication.getName());
    }
}
