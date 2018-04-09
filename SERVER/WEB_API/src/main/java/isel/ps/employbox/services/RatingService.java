package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Rating;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class RatingService {
    private final RapperRepository<Rating, Pair<Long, Long >> ratingRepo;
    private final RapperRepository<Account, Long> accountRepo;

    public RatingService(RapperRepository<Rating, Pair<Long, Long>> ratingRepo, RapperRepository<Account, Long> accountRepo) {
        this.ratingRepo = ratingRepo;
        this.accountRepo = accountRepo;
    }

    public Stream<Rating> getRatings(long accountId, String type) {
        return StreamSupport.stream(ratingRepo.findAll().spliterator(), false)
                .filter(curr-> type.equals("done") && curr.getAccountIDFrom() == accountId || type.equals("received")&& curr.getAccountIDTo()== accountId);
    }

    public Rating getRating(long accountFrom, long accountTo) {
        Optional<Rating> orating = ratingRepo.findById( new Pair<>(accountFrom, accountTo));
        if(!orating.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_Rating);
        return orating.get();
    }

    public void updateRating(Rating rating) {
        getRating(rating.getAccountIDFrom(), rating.getAccountIDTo());
        ratingRepo.update(rating);
    }

    public void createRating(Rating rating) {
        accountRepo.findById(rating.getAccountIDFrom());
        accountRepo.findById(rating.getAccountIDTo());
        ratingRepo.create( rating );
    }

    public void deleteRating(long accountIDFrom, long accountIDTo) {
        ratingRepo.delete( getRating(accountIDFrom, accountIDTo) );
    }
}
