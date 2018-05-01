package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.RatingBinder;
import isel.ps.employbox.model.entities.Rating;
import isel.ps.employbox.model.input.InRating;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutRating;
import isel.ps.employbox.services.RatingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

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
    public Mono<HalCollection> getRatings(@PathVariable long id, @RequestParam String type){
        if(type.equals("done") || type.equals("received"))
            return ratingBinder.bindOutput(
                    ratingService.getRatings(id, type),
                    this.getClass()
            );
        else
            throw new BadRequestException("Type must be either \"done\" or \"received\"");
    }

    @GetMapping("/single")
    public Mono<OutRating> getRating(@PathVariable long id, @RequestBody long accountTo){
        return ratingBinder.bindOutput(ratingService.getRating(id, accountTo));
    }

    @PutMapping
    public Mono<Void> updateRating(
            @PathVariable long id,
            @RequestParam long accountTo,
            @RequestBody InRating rating,
            Authentication authentication
    ){
        if(id != rating.getAccountIDFrom() || accountTo != rating.getAccountIDTo()) throw new BadRequestException(badRequest_IdsMismatch);
        return ratingService.updateRating(ratingBinder.bindInput(rating), authentication.getName());
    }

    @PostMapping
    public Mono<Rating> createRating(
            @PathVariable long id,
            @RequestParam long accountTo,
            @RequestBody InRating rating,
            Authentication authentication)
    {
        if(id != rating.getAccountIDFrom() || accountTo != rating.getAccountIDTo()) throw new BadRequestException(badRequest_IdsMismatch);
        return ratingService.createRating(ratingBinder.bindInput(rating), authentication.getName());
    }

    @DeleteMapping
    public Mono<Void> deleteRating(@PathVariable long id, @RequestParam long accountTo, Authentication authentication){
        return ratingService.deleteRating(id, accountTo, authentication.getName());
    }
}
