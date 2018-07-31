package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Rating;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

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
        UnitOfWork unitOfWork = new UnitOfWork();

        CompletableFuture<Rating> future = ratingRepo.findById(unitOfWork, new Rating.RatingKey(accountFrom, accountTo))
                .thenApply(orating -> {
                    if (!orating.isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_RATING);
                    return orating.get();
                })
                .thenCompose(rating -> unitOfWork.commit().thenApply(__ -> rating));
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateRating(Rating rating, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<Void> future = CompletableFuture.allOf(
                userAccountService.getUser(rating.getAccountIdFrom(), email),//throws exceptions
                getRating(rating.getAccountIdFrom(), rating.getAccountIdTo())
        )
                .thenCompose(aVoid -> ratingRepo.update(unitOfWork, rating))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Rating> createRating(Rating rating, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<Rating> future = CompletableFuture.allOf(
                userAccountService.getUser(rating.getAccountIdFrom(), email),
                userAccountService.getUser(rating.getAccountIdTo())
        )
                .thenCompose(aVoid -> ratingRepo.create(unitOfWork, rating))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> rating));
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteRating(long accountIDFrom, long accountIDTo, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<Void> future = userAccountService.getUser(accountIDFrom, email)
                .thenCompose(userAccount -> getRating(accountIDFrom, accountIDTo))
                .thenCompose(rating -> ratingRepo.delete(unitOfWork, rating))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
