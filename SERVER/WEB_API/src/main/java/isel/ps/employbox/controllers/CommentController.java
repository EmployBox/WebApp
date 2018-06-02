package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CommentBinder;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutComment;
import isel.ps.employbox.services.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<HalCollectionPage> getAllComments(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page
    ) {
        return commentBinder.bindOutput(
                commentService.getComments(id, page),
                this.getClass(),
                id
        );
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
        if(id != comment.getAccountIdFrom() || accountTo != comment.getAccountIdTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return commentService.updateComment(commentBinder.bindInput(comment), authentication.getName());
    }

    @PostMapping
    public Mono<OutComment> createComment(
            @PathVariable long accountFromId,
            @RequestParam long accountTo,
            @RequestBody InComment comment,
            Authentication authentication
    ){
        if(accountFromId != comment.getAccountIdFrom() || accountTo != comment.getAccountIdTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return commentBinder.bindOutput( commentService.createComment(commentBinder.bindInput(comment), authentication.getName()));
    }

    @DeleteMapping("{commentId}")
    public Mono<Void> deleteComment(@PathVariable long commentId, Authentication authentication){
        return commentService.deleteComment(commentId,  authentication.getName());
    }
}
