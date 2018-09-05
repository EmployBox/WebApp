package isel.ps.employbox.controllers.account;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.CommentBinder;
import isel.ps.employbox.model.entities.Comment;
import isel.ps.employbox.model.input.InComment;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutComment;
import isel.ps.employbox.services.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/{accountId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentBinder commentBinder;

    public CommentController(CommentService commentService, CommentBinder commentBinder) {
        this.commentService = commentService;
        this.commentBinder = commentBinder;
    }

    @GetMapping
    public Mono<HalCollectionPage<Comment>> getAllComments(
            @PathVariable long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    ) {
        CompletableFuture<HalCollectionPage<Comment>> future = commentService.getComments(accountId, page, pageSize, orderColumn, orderClause)
                .thenCompose(commentCollectionPage -> commentBinder.bindOutput(commentCollectionPage, this.getClass(), accountId));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{commentId}")
    public Mono<OutComment> getComment(@PathVariable long accountId, @PathVariable long commentId){
        CompletableFuture<OutComment> future = commentService.getComment(accountId, commentId)
                .thenCompose(commentBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @GetMapping("/{commentId}/replies")
    public Mono<HalCollectionPage<Comment>> getCommentReplies(@PathVariable long accountId, @PathVariable long commentId){
        CompletableFuture<HalCollectionPage<Comment>> future = commentService.getCommentReplies(accountId, commentId)
                .thenCompose(commentCollectionPage -> commentBinder.bindOutput(commentCollectionPage, this.getClass(), accountId));
        return Mono.fromFuture(future);
    }

    @PutMapping("/{commentId}")
    public Mono<Void> updateComment(
            @PathVariable long accountId,
            @PathVariable long commentId,
            @RequestBody InComment inComment,
            Authentication authentication
    ){
        if(accountId != inComment.getAccountIdTo() || commentId != inComment.getCommmentId()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return commentService.updateComment(commentBinder.bindInput(inComment),authentication.getName());
    }

    @PostMapping
    public Mono<OutComment> createComment(
            @PathVariable long accountId,
            @RequestBody InComment inComment,
            Authentication authentication
    ){
        if(accountId != inComment.getAccountIdTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Comment comment = commentBinder.bindInput(inComment);
        CompletableFuture<OutComment> future = commentService.createComment(comment, authentication.getName())
                .thenCompose(commentBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @DeleteMapping("/{commentId}")
    public Mono<Void> deleteComment(@PathVariable long commentId, Authentication authentication){
        return commentService.deleteComment(commentId,  authentication.getName());
    }
}
