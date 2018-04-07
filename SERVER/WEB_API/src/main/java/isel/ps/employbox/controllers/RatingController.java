package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.RatingBinder;
import isel.ps.employbox.model.input.InRating;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutRating;
import isel.ps.employbox.services.RatingService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

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
    public Resource<HalCollection> getRatings(@PathVariable long id, @RequestParam String type){
        if(type.equals("done") || type.equals("received"))
            return ratingBinder.bindOutput(
                    ratingService.getRatings(id, type),
                    this.getClass(),
                    id
            );
        else
            throw new BadRequestException("Type must be either \"done\" or \"received\"");
    }

    @GetMapping
    public Resource<OutRating> getRating(@PathVariable long id, @RequestParam long accountTo){
        return ratingBinder.bindOutput(ratingService.getRating(id, accountTo));
    }

    @PutMapping
    public void updateRating(@PathVariable long id, @RequestParam long accountTo, @RequestBody InRating rating){
        if(id != rating.getAccountIDFrom() || accountTo != rating.getAccountIDTo()) throw new BadRequestException(badRequest_IdsMismatch);
        ratingService.updateRating(ratingBinder.bindInput(rating));
    }

    @PostMapping
    public void createRating(@PathVariable long id, @RequestParam long accountTo, @RequestBody InRating rating){
        if(id != rating.getAccountIDFrom() || accountTo != rating.getAccountIDTo()) throw new BadRequestException(badRequest_IdsMismatch);
        ratingService.createRating(ratingBinder.bindInput(rating));
    }

    @DeleteMapping
    public void deleteRating(@PathVariable long id, @RequestParam long accountTo){
        ratingService.deleteRating(id, accountTo);
    }
}
