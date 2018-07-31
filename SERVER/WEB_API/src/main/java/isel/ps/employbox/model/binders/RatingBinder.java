package isel.ps.employbox.model.binders;

import isel.ps.employbox.model.entities.Rating;
import isel.ps.employbox.model.input.InRating;
import isel.ps.employbox.model.output.OutRating;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class RatingBinder implements ModelBinder<Rating,OutRating,InRating> {

    @Override
    public CompletableFuture<OutRating> bindOutput(Rating rating) {
        return CompletableFuture.completedFuture(new OutRating(
                rating.getAccountIdFrom(),
                rating.getAccountIdTo(),
                rating.getWorkLoad(),
                rating.getWage(),
                rating.getWorkEnviroment(),
                rating.getCompetences(),
                rating.getPonctuality(),
                rating.getAssiduity(),
                rating.getDemeanor()
        ));
    }

    @Override
    public Rating bindInput(InRating object) {
        return new Rating(
                object.getAccountIDFrom(),
                object.getAccountIDTo(),
                object.getWorkLoad(),
                object.getWage(),
                object.getWorkEnvironment(),
                object.getCompetence(),
                object.getPontuality(),
                object.getAssiduity(),
                object.getDemeanor(),
                -1);
    }
}
