package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.exceptions.BadRequestException;
import isel.ps.employbox.api.model.ModelBinder;
import isel.ps.employbox.api.model.input.InRating;
import isel.ps.employbox.api.model.output.OutRating;
import isel.ps.employbox.api.services.RatingService;
import isel.ps.employbox.dal.model.Rating;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static isel.ps.employbox.api.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
public class RatingController {

    private final ModelBinder<Rating, OutRating, InRating, String> ratingBinder;
    private final RatingService ratingService;

    public RatingController(ModelBinder<Rating, OutRating, InRating, String> ratingBinder, RatingService ratingService) {
        this.ratingBinder = ratingBinder;
        this.ratingService = ratingService;
    }

    @GetMapping("/account/{id}/rating")
    public List<OutRating> getRatings(@PathVariable long id, @RequestParam String type){
        if(type.equals("done") || type.equals("received"))
            return ratingBinder.bindOutput(ratingService.getRatings(id, type));
        else
            throw new BadRequestException("Type must be either \"done\" or \"received\"");
    }

    @GetMapping("/account/{id}/rating")
    public Optional<OutRating> getRating(@PathVariable long id, @RequestParam long accountTo){
        return ratingService.getRating(id, accountTo)
                .map(ratingBinder::bindOutput);
    }

    @PutMapping("/account/{id}/rating")
    public void updateRating(@PathVariable long id, @RequestParam long accountTo, @RequestBody InRating rating){
        if(id != rating.getAccountIDFrom() || accountTo != rating.getAccountIDTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        ratingService.updateRating(ratingBinder.bindInput(rating));
    }

    @PostMapping("/account/{id}/rating")
    public void createRating(@PathVariable long id, @RequestParam long accountTo, @RequestBody InRating rating){
        if(id != rating.getAccountIDFrom() || accountTo != rating.getAccountIDTo()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        ratingService.createRating(ratingBinder.bindInput(rating));
    }

    @DeleteMapping("/account/{id}/rating")
    public void deleteRating(@PathVariable long id, @RequestParam long accountTo){
        ratingService.deleteRating(id, accountTo);
    }
}
