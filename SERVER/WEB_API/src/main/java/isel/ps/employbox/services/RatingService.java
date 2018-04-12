package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Rating;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class RatingService {
    private final RapperRepository<Rating, Pair<Long, Long >> ratingRepo;
    private final UserService userService;

    public RatingService(RapperRepository<Rating, Pair<Long, Long>> ratingRepo, UserService userService) {
        this.ratingRepo = ratingRepo;
        this.userService = userService;
    }

    public Stream<Rating> getRatings(long accountId, String type) {
        return StreamSupport.stream(ratingRepo.findAll().join().spliterator(), false)
                .filter(curr-> type.equals("done") && curr.getAccountIDFrom() == accountId || type.equals("received")&& curr.getAccountIDTo()== accountId);
    }

    public Rating getRating(long accountFrom, long accountTo) {
        Optional<Rating> orating = ratingRepo.findById( new Pair<>(accountFrom, accountTo)).join();
        if(!orating.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_Rating);
        return orating.get();
    }

    public void updateRating(Rating rating, String email) {
        userService.getUser(rating.getAccountIDFrom(), email);//throws exceptions
        getRating(rating.getAccountIDFrom(), rating.getAccountIDTo());
        ratingRepo.update(rating);
    }

    public void createRating(Rating rating, String email) {
        userService.getUser(rating.getAccountIDFrom(),email);
        userService.getUser(rating.getAccountIDTo());
        ratingRepo.create( rating );
    }

    public void deleteRating(long accountIDFrom, long accountIDTo, String email) {
        ratingRepo.delete( getRating(accountIDFrom, accountIDTo) );
    }
}
