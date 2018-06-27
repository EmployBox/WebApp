package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Rating;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RatingService {
    private final DataRepository<Rating, Rating.RatingKey> ratingRepo;
    private final UserAccountService userAccountService;
    private final AccountService accountService;

    public RatingService(DataRepository<Rating, Rating.RatingKey> ratingRepo, UserAccountService userAccountService, AccountService accountService) {
        this.ratingRepo = ratingRepo;
        this.userAccountService = userAccountService;
        this.accountService = accountService;
    }

    public CompletableFuture<CollectionPage<Rating>> getRatings(long accountId, int page, int pageSize) {
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add( new Pair("accountId", accountId));
        Pair[] query = pairs.stream()
                .filter(stringStringPair -> stringStringPair.getValue() != null)
                .toArray(Pair[]::new);
        return ServiceUtils.getCollectionPageFuture(
                ratingRepo, page, pageSize, query);
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
                        .thenCompose(aVoid -> ratingRepo.update(rating))
        );
    }

    public Mono<Rating> createRating(Rating rating, String email) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        userAccountService.getUser(rating.getAccountIdFrom(), email),
                        userAccountService.getUser(rating.getAccountIdTo())
                )
                        .thenCompose(aVoid -> ratingRepo.create(rating))
                        .thenApply(res -> rating)
        );
    }

    public Mono<Void> deleteRating(long accountIDFrom, long accountIDTo, String email) {
        return Mono.fromFuture(
                userAccountService.getUser(accountIDFrom, email)
                        .thenCompose(userAccount-> getRating(accountIDFrom, accountIDTo))
                        .thenAccept(ratingRepo::delete)
        );
    }
}
