package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.ModelBinder;
import isel.ps.employbox.api.model.input.InRating;
import isel.ps.employbox.api.model.output.OutRating;
import isel.ps.employbox.dal.model.Rating;
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
