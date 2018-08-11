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
@RequestMapping("/accounts/{id}/ratings")
public class RatingController {

    private final RatingBinder ratingBinder;
    private final RatingService ratingService;

    public RatingController(RatingBinder ratingBinder, RatingService ratingService) {
        this.ratingBinder = ratingBinder;
        this.ratingService = ratingService;
    }

    @GetMapping
    public Mono<HalCollectionPage<Rating>> getRatings(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ){
        CompletableFuture<HalCollectionPage<Rating>> future = ratingService.getRatings(id, page, pageSize)
                .thenCompose(ratingCollectionPage -> ratingBinder.bindOutput(ratingCollectionPage, this.getClass(), id));
        return Mono.fromFuture(future);
    }

    @GetMapping("/single")
    public Mono<OutRating> getRating(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") long accountIdDest
    ){
        if(accountIdDest == 0) throw new BadRequestException(ErrorMessages.BAD_REQUEST_UPDATE_RATING);
        CompletableFuture<OutRating> future = ratingService.getRating(id, accountIdDest)
                .thenCompose(ratingBinder::bindOutput);

        return Mono.fromFuture(future);
    }

    @PutMapping
    public Mono<Void> updateRating(
            @PathVariable long id,
            @RequestParam long accountTo,
            @RequestBody InRating rating,
            Authentication authentication
    ){
        if(id != rating.getAccountIDFrom() || accountTo != rating.getAccountIDTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return ratingService.updateRating(ratingBinder.bindInput(rating), authentication.getName());
    }

    @PostMapping
    public Mono<Rating> createRating(
            @PathVariable long id,
            @RequestParam long accountTo,
            @RequestBody InRating rating,
            Authentication authentication)
    {
        if(id != rating.getAccountIDFrom() || accountTo != rating.getAccountIDTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return ratingService.createRating( ratingBinder.bindInput(rating), authentication.getName());
    }

    @DeleteMapping
    public Mono<Void> deleteRating(@PathVariable long id, @RequestParam long accountTo, Authentication authentication){
        return ratingService.deleteRating(id, accountTo, authentication.getName());
    }
}
