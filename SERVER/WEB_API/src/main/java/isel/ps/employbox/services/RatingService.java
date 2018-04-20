package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Rating;
import org.github.isel.rapper.DataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class RatingService {
    private final DataRepository<Rating, Rating.RatingKey> ratingRepo;
    private final UserService userService;

    public RatingService(DataRepository<Rating, Rating.RatingKey> ratingRepo, UserService userService) {
        this.ratingRepo = ratingRepo;
        this.userService = userService;
    }

    public Stream<Rating> getRatings(long accountId, String type) {
        return StreamSupport.stream(ratingRepo.findAll().join().spliterator(), false)
                .filter(curr-> type.equals("done") && curr.getAccountIdFrom() == accountId || type.equals("received")&& curr.getAccountIdTo()== accountId);
    }

    public Rating getRating(long accountFrom, long accountTo) {
        Optional<Rating> orating = ratingRepo.findById( new Rating.RatingKey(accountFrom, accountTo)).join();
        if(!orating.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_Rating);
        return orating.get();
    }

    public void updateRating(Rating rating, String email) {
        userService.getUser(rating.getAccountIdFrom(), email);//throws exceptions
        getRating(rating.getAccountIdFrom(), rating.getAccountIdTo());
        ratingRepo.update(rating);
    }

    public void createRating(Rating rating, String email) {
        userService.getUser(rating.getAccountIdFrom(),email);
        userService.getUser(rating.getAccountIdTo());
        ratingRepo.create( rating );
    }

    public void deleteRating(long accountIDFrom, long accountIDTo, String email) {
        ratingRepo.delete( getRating(accountIDFrom, accountIDTo) );
    }
}
