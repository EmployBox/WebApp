package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ForbiddenException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Rating;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class RatingService {
    private final AccountService accountService;

    public RatingService(AccountService userAccountService) {
        this.accountService = userAccountService;
    }

    public CompletableFuture<CollectionPage<Rating>> getRatings(long accountId, int page, int pageSize, String orderColumn, String orderClause) {
        ArrayList<Condition> conditions = new ArrayList<>();
        ServiceUtils.evaluateOrderClause(orderColumn,orderClause, conditions);
        conditions.add( new EqualAndCondition<>("accountIdTo", accountId));

        return ServiceUtils.getCollectionPageFuture(Rating.class, page, pageSize,  conditions.toArray(new Condition[conditions.size()]));
    }


    public CompletableFuture<Rating> getRating(long accountIdDest, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);

        CompletableFuture<Rating> future =
                accountService.getAccount(email)
                        .thenCompose(account ->
                                ratingMapper.findById(new Rating.RatingKey(account.getIdentityKey(), accountIdDest))
                                        .thenCompose(ratings -> {
                                                    ratings.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_RATING));
                                                    return unitOfWork.commit().thenApply(__ -> ratings.get());
                                                }
                                        )
                        );
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateRating(Rating rating, String email) {

        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        CompletableFuture<Void> future =
                accountService.getAccount(rating.getAccountIdFrom(), email)//throws exceptions
                        .thenCompose(user -> {
                            if(user.getIdentityKey() != rating.getAccountIdFrom())
                                throw new ForbiddenException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                            return ratingMapper.find(new EqualAndCondition("accountIdFrom", rating.getAccountIdFrom()),
                                    new EqualAndCondition<>("accountIdTo", rating.getAccountIdTo()));
                        })
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


    public Mono<Rating> createRating(Rating rating,String ratingType, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);
        Account[] account = new Account[1];

        CompletableFuture<Rating> future = CompletableFuture.allOf(
                accountService.getAccount(email, unitOfWork).thenAccept( acc -> {
                    if(acc.getIdentityKey() != rating.getAccountIdFrom())
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                }),
                accountService.getAccount(rating.getAccountIdTo()).thenAccept(acc -> account[0] = acc)
        )
                .thenCompose(aVoid -> ratingMapper.create(rating))
                .thenCompose(aVoid -> ratingMapper.find( new EqualAndCondition<>("accountIdTo", account[0].getIdentityKey())))
                .thenCompose( list -> {
                    int [] ratingAverage = new int[1];
                    list.forEach( curr -> {
                                if (ratingType.compareTo("USR") == 0)
                                    ratingAverage[0] += (curr.getAssiduity()
                                            + curr.getCompetence()
                                            + curr.getDemeanor()
                                            + curr.getPonctuality()) / 4;
                                else if(ratingType.compareTo("CMP") == 0)
                                    ratingAverage[0] += (
                                            + curr.getWage()
                                            + curr.getWorkLoad()
                                            + curr.getWorkEnviroment()) / 3;
                                else throw new BadRequestException(ErrorMessages.INVALID_ACCOUNT_TYPE_IN_RATING);
                            }
                    );
                    ratingAverage[0] /= list.size();
                    account[0].rating = ratingAverage[0];
                    return accountMapper.update(account[0]);
                })
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> rating));
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteRating(long accountIdDest, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unitOfWork);
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);
        CompletableFuture<Void> future = accountMapper.find( new EqualAndCondition<>("email", email))
                .thenCompose(account -> {
                            if (account.size() != 1)
                                    throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                                return ratingMapper.find(new EqualAndCondition("accountIdFrom", account.get(0).getIdentityKey()),
                                        new EqualAndCondition<>("accountIdTo", accountIdDest));
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
