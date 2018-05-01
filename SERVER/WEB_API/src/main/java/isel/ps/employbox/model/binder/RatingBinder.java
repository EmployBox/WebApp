package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Rating;
import isel.ps.employbox.model.input.InRating;
import isel.ps.employbox.model.output.OutRating;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class RatingBinder implements ModelBinder<Rating,OutRating,InRating> {

    @Override
    public Mono<OutRating> bindOutput(CompletableFuture<Rating> ratingCompletableFuture) {
        return Mono.fromFuture(
                ratingCompletableFuture.thenApply(
                        rating -> new OutRating(
                                rating.getAccountIdFrom(),
                                rating.getAccountIdTo(),
                                rating.getWorkLoad(),
                                rating.getWage(),
                                rating.getWorkEnvironment(),
                                rating.getCompetence(),
                                rating.getPontuality(),
                                rating.getAssiduity(),
                                rating.getDemeanor())
                )
        );
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
