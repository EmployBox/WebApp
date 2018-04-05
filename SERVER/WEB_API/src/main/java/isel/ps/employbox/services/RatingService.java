package isel.ps.employbox.services;

import isel.ps.employbox.model.entities.Rating;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.stream.Stream;

@Service
public class RatingService {

    public Stream<Rating> getRatings(long id, String type) {
        throw new NotImplementedException();
    }

    public Rating getRating(long id, long accountTo) {
        return null;
    }

    public void updateRating(Rating rating) {

    }

    public void createRating(Rating rating) {

    }

    public void deleteRating(long id, long accountTo) {

    }
}
