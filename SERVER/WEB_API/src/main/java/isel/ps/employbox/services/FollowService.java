package isel.ps.employbox.services;


import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.mapper.conditions.EqualOrCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Follows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class FollowService {
    private final AccountService accountService;


    public FollowService(AccountService accountRepo) {
        this.accountService = accountRepo;
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFollowers(long followedAccountId, int page, int pageSize, String orderColumn, String orderClause) {
        return getAccountFromFollowAux(followedAccountId, "accountIdFollower", page, pageSize, orderColumn, orderClause);
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFolloweds(long followerAccountId, int page, int pageSize, String orderColumn, String orderClause) {
        return getAccountFromFollowAux(followerAccountId, "accountIdFollowed", page, pageSize, orderColumn, orderClause);
    }

    private CompletableFuture<CollectionPage<Account>> getAccountFromFollowAux(long followId, String collumn, int page, int pageSize, String orderColumn, String orderClause) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follows, Follows.FollowKey> followMapper = getMapper(Follows.class, unitOfWork);
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);

        long [] numberOfFolloewEntries = new long[1];
        Condition followsQuery = new EqualAndCondition<>(collumn, followId);

        //todo must
        CompletableFuture future =
                followMapper.getNumberOfEntries(followsQuery).thenCompose(
                        numberOfEntries -> {
                            numberOfFolloewEntries[0] = numberOfEntries;
                            int entriesNumber = numberOfEntries.intValue();
                            if (entriesNumber <= pageSize)
                                entriesNumber = pageSize;

                            return followMapper.find(0, entriesNumber, followsQuery);
                        }
                )
                        .thenCompose(follow -> {
                            List<Condition> conditionPairs = new ArrayList<>();
                            for (int i = 0; i < follow.size(); i++)
                                if (collumn.compareTo("accountIdFollower") == 0)
                                    conditionPairs.add(new EqualOrCondition<Long>("accountId", follow.get(i).getAccountIdFollowed()));
                                else
                                    conditionPairs.add(new EqualOrCondition<Long>("accountId", follow.get(i).getAccountIdFollower()));

                            ServiceUtils.evaluateOrderClause(orderColumn, orderClause, conditionPairs);

                            return accountMapper.find(page, pageSize, conditionPairs.toArray(new Condition[conditionPairs.size()]));
                        }).thenCompose(accountsList -> unitOfWork.commit().thenApply(aVoid -> accountsList))
                        .thenApply(accountsList -> new CollectionPage<Account>(numberOfFolloewEntries[0], pageSize, page, accountsList));

        return handleExceptions(future, unitOfWork);
    }


    public Mono<Void> createFollower(long accountId, long accountToBeFollowedId, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follows, Follows.FollowKey> followMapper = getMapper(Follows.class, unitOfWork);
        CompletableFuture<Void> future = accountService.getAccount(accountId, username)
                .thenCompose(account -> followMapper.create(new Follows(accountId, accountToBeFollowedId)))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteFollower(long accountId, long followedId, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follows, Follows.FollowKey> followMapper = getMapper(Follows.class, unitOfWork);
        CompletableFuture<Void> future = accountService.getAccount(accountId, username)
                .thenCompose(account -> followMapper.deleteById(new Follows.FollowKey(accountId, followedId)))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public CompletableFuture<CollectionPage<Account>> checkIfAccountIsFollowerOf(long accountId, long supposedFollowerAccountId) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follows, Follows.FollowKey> followMapper = getMapper(Follows.class, unitOfWork);
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);

        return followMapper.findById(new Follows.FollowKey(accountId, supposedFollowerAccountId))
                .thenCompose( follows ->{
                    if(follows.isPresent())
                        return accountMapper.findById(supposedFollowerAccountId);
                    else
                        return CompletableFuture.completedFuture(Optional.empty());

                })
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(
                        oaccount -> {
                            ArrayList<Account> list = new ArrayList<>();
                            oaccount.ifPresent(list::add);

                            return new CollectionPage<Account>(list.size(), 1,0, list);
                        }
                );
    }

    public CompletableFuture<CollectionPage<Account>> checkIfAccountIsFollowing(long accountId, long supposedFollowingAccountId) {
        return checkIfAccountIsFollowerOf(supposedFollowingAccountId, accountId);
    }
}
