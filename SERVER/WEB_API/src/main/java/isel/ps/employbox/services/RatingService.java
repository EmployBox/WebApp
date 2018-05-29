package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Rating;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class RatingService {
    private final DataRepository<Rating, Rating.RatingKey> ratingRepo;
    private final UserAccountService userAccountService;

    public RatingService(DataRepository<Rating, Rating.RatingKey> ratingRepo, UserAccountService userAccountService) {
        this.ratingRepo = ratingRepo;
        this.userAccountService = userAccountService;
    }

    public CompletableFuture<Stream<Rating>> getRatings(long accountId, String type) {
        return ratingRepo.findWhere(new Pair<>("accountId", accountId))
                .thenApply(list ->
                        list.stream()
                                .filter(curr-> type.equals("done") && curr.getAccountIdFrom() == accountId || type.equals("received")&& curr.getAccountIdTo()== accountId)

                );
    }

    public CompletableFuture<Rating> getRating(long accountFrom, long accountTo) {
        return ratingRepo.findById( new Rating.RatingKey(accountFrom, accountTo))
                .thenApply( orating -> {
                    if(!orating.isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_RATING);
                    return orating.get();
                });
    }

    public Mono<Void> updateRating(Rating rating, String email) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        userAccountService.getUser(rating.getAccountIdFrom(), email),//throws exceptions
                        getRating(rating.getAccountIdFrom(), rating.getAccountIdTo())
                )
                .thenCompose (__-> ratingRepo.update(rating))
                .thenAccept(res -> {
                    if(res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                })
        );
    }

    public Mono<Rating> createRating(Rating rating, String email) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        userAccountService.getUser(rating.getAccountIdFrom(),email),
                        userAccountService.getUser(rating.getAccountIdTo())
                )
                .thenCompose ( __-> ratingRepo.create( rating ))
                .thenApply(res -> {
                    if(res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                    return rating;
                })
        );
    }

    public Mono<Void> deleteRating(long accountIDFrom, long accountIDTo, String email) {
        return Mono.fromFuture(
                userAccountService.getUser(accountIDFrom, email)
                        .thenCompose(__-> getRating(accountIDFrom, accountIDTo))
                        .thenAccept( rating -> ratingRepo.delete(rating))
        );
    }
}
