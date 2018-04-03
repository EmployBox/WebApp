package isel.ps.employbox.services;

import isel.ps.employbox.model.entities.Rating;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    public List<Rating> getRatings(long id, String type) {
        throw new NotImplementedException();
    }

    public Optional<Rating> getRating(long id, long accountTo) {
        return Optional.empty();
    }

    public void updateRating(Rating rating) {

    }

    public void createRating(Rating rating) {

    }

    public void deleteRating(long id, long accountTo) {

    }
}
