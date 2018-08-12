package isel.ps.employbox.controllers;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.RatingBinder;
import isel.ps.employbox.model.entities.Rating;
import isel.ps.employbox.model.input.InRating;
import isel.ps.employbox.model.output.HalCollectionPage;
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
            @RequestParam(defaultValue = "5") int pageSize
    ){
        CompletableFuture<HalCollectionPage<Rating>> future = ratingService.getRatings(accountId, page, pageSize)
                .thenCompose(ratingCollectionPage -> ratingBinder.bindOutput(ratingCollectionPage, this.getClass(), accountId));
        return Mono.fromFuture(future);
    }

    @GetMapping("/single")
    public Mono<OutRating> getRating(
            @PathVariable long accountId,
            @RequestParam(defaultValue = "0") long accountIdDest
    ){
        if(accountIdDest == 0) throw new BadRequestException(ErrorMessages.BAD_REQUEST_UPDATE_RATING);
        CompletableFuture<OutRating> future = ratingService.getRating(accountId, accountIdDest)
                .thenCompose(ratingBinder::bindOutput);

        return Mono.fromFuture(future);
    }

    @PutMapping
    public Mono<Void> updateRating(
            @PathVariable long accountId,
            @RequestParam long accountIdDest,
            @RequestBody InRating rating,
            Authentication authentication
    ){
        if(accountId != rating.getAccountIdFrom() || accountIdDest != rating.getAccountIdDest()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return ratingService.updateRating(ratingBinder.bindInput(rating), authentication.getName());
    }

    @PostMapping
    public Mono<Rating> createRating(
            @PathVariable long accountId,
            @RequestParam long accountIdDest,
            @RequestBody InRating rating,
            Authentication authentication)
    {
        if(accountId != rating.getAccountIdFrom() || accountIdDest != rating.getAccountIdDest()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return ratingService.createRating( ratingBinder.bindInput(rating), authentication.getName());
    }

    @DeleteMapping
    public Mono<Void> deleteRating(
            @PathVariable long accountId,
            @RequestParam long accountIdDest,
            Authentication authentication){
        return ratingService.deleteRating(accountId, accountIdDest, authentication.getName());
    }
}
