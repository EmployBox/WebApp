package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
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
    private final AccountService accountService;

    public RatingService(AccountService userAccountService) {
        this.accountService = userAccountService;
    }

    public CompletableFuture<CollectionPage<Rating>> getRatings(long accountId, int page, int pageSize) {
        return ServiceUtils.getCollectionPageFuture(Rating.class, page, pageSize,  new EqualAndCondition<>("accountIdFrom", accountId));
    }

    public CompletableFuture<Rating> getRating(long accountIdFrom, long accountIdDest) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        CompletableFuture<Rating> future = ratingMapper.find(new EqualAndCondition<>("accountIdFrom", accountIdFrom), new EqualAndCondition<>("accountIdDest", accountIdDest))
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
                accountService.getAccount(rating.getAccountIdFrom(), email)//throws exceptions
                        .thenCompose(user -> ratingMapper.find(new EqualAndCondition("accountIdFrom", rating.getAccountIdFrom()),
                                new EqualAndCondition<>("accountIdDest", rating.getAccountIdTo()))
                        )
                        .thenCompose(ratings -> {
                                    if (ratings.size() == 0)
                                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND);
                                    return ratingMapper.update(rating);
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
                accountService.getAccount(rating.getAccountIdFrom(), email),
                accountService.getAccount(rating.getAccountIdTo())
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
        CompletableFuture<Void> future = accountMapper.find(new EqualAndCondition<Long>("accountId", accountIDFrom), new EqualAndCondition<>("email", email))
                .thenCompose(user -> {
                            if (user.size() != 1)
                                    throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                                return ratingMapper.find(new EqualAndCondition("accountIdFrom", accountIDFrom),
                                        new EqualAndCondition<>("accountIdDest", accountIDTo));
                        }
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
