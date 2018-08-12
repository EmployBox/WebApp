package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Rating;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class RatingService {
    private final UserAccountService userAccountService;

    public RatingService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    public CompletableFuture<CollectionPage<Rating>> getRatings(long accountId, int page, int pageSize) {
        return ServiceUtils.getCollectionPageFuture(Rating.class, page, pageSize,  new EqualCondition<>("accountIdFrom", accountId));
    }

    public CompletableFuture<Rating> getRating(long accountIdFrom, long accountIdDest) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        CompletableFuture<Rating> future = ratingMapper.find(new EqualCondition<>("accountIdFrom", accountIdFrom), new EqualCondition<>("accountIdDest", accountIdDest))
                .thenCompose(ratings -> {
                    if ( ratings.size()==0)
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_RATING);
                    return  unitOfWork.commit().thenApply(__ -> ratings.get(0));
                });
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateRating(Rating rating, String email) {

        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        CompletableFuture<Void> future =
                userAccountService.getUser(rating.getAccountIdFrom(), email)//throws exceptions
                        .thenCompose(user -> ratingMapper.find(new EqualCondition("accountIdFrom", rating.getAccountIdFrom()),
                                new EqualCondition<>("accountIdDest", rating.getAccountIdTo()))
                        )
                        .thenCompose(ratings -> {
                                    if (ratings.size() == 0)
                                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND);
                                    return ratingMapper.update(ratings.get(0));
                                }
                        )
                        .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Rating> createRating(Rating rating, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        CompletableFuture<Rating> future = CompletableFuture.allOf(
                userAccountService.getUser(rating.getAccountIdFrom(), email),
                userAccountService.getUser(rating.getAccountIdTo())
        )
                .thenCompose(aVoid -> ratingMapper.create(rating))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> rating));
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteRating(long accountIDFrom, long accountIDTo, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);
        CompletableFuture<Void> future = accountMapper.find(new EqualCondition<Long>("accountId", accountIDFrom), new EqualCondition<>("email", email))
                .thenCompose(user -> ratingMapper.find(new EqualCondition("accountIdFrom", accountIDFrom),
                        new EqualCondition<>("accountIdDest", accountIDTo))
                )
                .thenCompose(ratings -> {
                    if (ratings.size() == 0)
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND);
                    return  ratingMapper.delete(ratings.get(0));
                })
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
