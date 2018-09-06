package isel.ps.employbox.controllers.account;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.RatingBinder;
import isel.ps.employbox.model.entities.Rating;
import isel.ps.employbox.model.input.InRating;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutRating;
import isel.ps.employbox.services.RatingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/{accountId}/ratings")
public class RatingController {

    private final RatingBinder ratingBinder;
    private final RatingService ratingService;

    public RatingController(RatingBinder ratingBinder, RatingService ratingService) {
        this.ratingBinder = ratingBinder;
        this.ratingService = ratingService;
    }

    @GetMapping
    public Mono<HalCollectionPage<Rating>> getRatings(
            @PathVariable long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    ){
        CompletableFuture<HalCollectionPage<Rating>> future = ratingService.getRatings(accountId, page, pageSize,orderColumn, orderClause)
                .thenCompose(ratingCollectionPage -> ratingBinder.bindOutput(ratingCollectionPage, this.getClass(), accountId));
        return Mono.fromFuture(future);
    }

    @GetMapping("/single")
    public Mono<OutRating> getRating(
            @PathVariable long accountId,
            Authentication authentication
    ){

        CompletableFuture<OutRating> future = ratingService.getRating( accountId, authentication.getName())
                .thenCompose(ratingBinder::bindOutput);

        return Mono.fromFuture(future);
    }

    @PutMapping
    public Mono<Void> updateRating(
            @PathVariable long accountId,
            @RequestBody InRating inRating,
            Authentication authentication
    ){
        if(accountId != inRating.getAccountIdTo())
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return ratingService.updateRating(inRating.getAccountType(), ratingBinder.bindInput(inRating), authentication.getName());
    }

    @PostMapping
    public Mono<Rating> createRating(
            @PathVariable long accountId,
            @RequestBody InRating inRating,
            Authentication authentication)
    {
        if(accountId != inRating.getAccountIdTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return ratingService.createRating( ratingBinder.bindInput(inRating), inRating.getAccountType(), authentication.getName());
    }

    @DeleteMapping
    public Mono<Void> deleteRating(
            @PathVariable long accountId,
            Authentication authentication){
        return ratingService.deleteRating(accountId, authentication.getName());
    }
}
