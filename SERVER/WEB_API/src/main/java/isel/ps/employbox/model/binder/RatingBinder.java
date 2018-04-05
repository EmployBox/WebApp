package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Rating;
import isel.ps.employbox.model.input.InRating;
import isel.ps.employbox.model.output.OutRating;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class RatingBinder extends ModelBinder<Rating, OutRating, InRating> {


    @Override
    public Stream<Rating> bindInput(Stream<InRating> list) {
        return null;
    }

    @Override
    public Resource<OutRating> bindOutput(Rating object) {
        return null;
    }

    @Override
    public Rating bindInput(InRating object) {
        return null;
    }
}
