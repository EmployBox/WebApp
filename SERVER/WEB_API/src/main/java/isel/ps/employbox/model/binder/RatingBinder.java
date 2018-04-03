package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.ModelBinder;
import isel.ps.employbox.model.input.InRating;
import isel.ps.employbox.model.output.OutRating;
import isel.ps.employbox.model.entities.Rating;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatingBinder implements ModelBinder<Rating, OutRating, InRating, String> {
    @Override
    public List<OutRating> bindOutput(List<Rating> list) {
        return null;
    }

    @Override
    public List<Rating> bindInput(List<InRating> list) {
        return null;
    }

    @Override
    public OutRating bindOutput(Rating object) {
        return null;
    }

    @Override
    public Rating bindInput(InRating object) {
        return null;
    }
}
