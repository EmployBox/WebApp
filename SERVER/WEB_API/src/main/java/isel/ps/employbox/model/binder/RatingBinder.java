package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Rating;
import isel.ps.employbox.model.input.InRating;
import isel.ps.employbox.model.output.OutRating;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

@Component
public class RatingBinder extends ModelBinder<Rating, OutRating, InRating> {

    @Override
    public Resource<OutRating> bindOutput(Rating object) {
        return new Resource<>(new OutRating(
                object.getAccountIDFrom(),
                object.getAccountIDTo(),
                object.getWorkLoad(),
                object.getWage(),
                object.getWorkEnvironment(),
                object.getCompetence(),
                object.getPontuality(),
                object.getAssiduity(),
                object.getDemeanor())
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
                object.getDemeanor()
        );
    }
}
