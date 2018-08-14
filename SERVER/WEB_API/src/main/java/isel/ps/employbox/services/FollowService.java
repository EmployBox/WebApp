package isel.ps.employbox.services;


import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Follows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class FollowService {
    private final AccountService accountService;


    public FollowService(AccountService accountRepo) {
        this.accountService = accountRepo;
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFollowers(long followedAccountId, int page, int pageSize) {
        return getAccountFromFollowAux(followedAccountId, "accountIdFollower", page, pageSize);
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFolloweds(long followerAccountId, int page, int pageSize) {
        return getAccountFromFollowAux(followerAccountId, "accountIdFollowed", page, pageSize);
    }

    private CompletableFuture<CollectionPage<Account>> getAccountFromFollowAux(long followId, String collumn, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follows, Follows.FollowKey> followMapper = getMapper(Follows.class, unitOfWork);
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);

        long [] numberOfFolloewEntries = new long[1];
        Condition followsQuery = new EqualCondition<>(collumn, followId);
        ArrayList<CompletableFuture<List<Account>>> accountsFutures = new ArrayList<>();

        CompletableFuture future =
                followMapper.getNumberOfEntries(followsQuery).thenCompose(
                        numberOfEntries -> {
                            numberOfFolloewEntries[0] = numberOfEntries;
                            return followMapper.find(page, pageSize, followsQuery);
                        }
                )
                        .thenCompose(follow -> {

                            for (int i = 0; i < follow.size(); i++)
                                if (collumn.compareTo("accountIdFollowed") == 0)
                                    accountsFutures.add(accountMapper.find(page, pageSize, new EqualCondition<Long>("accountId", follow.get(i).getAccountIdFollowed())));
                                else
                                    accountsFutures.add(accountMapper.find(page, pageSize, new EqualCondition<Long>("accountId", follow.get(i).getAccountIdFollower())));

                            return CompletableFuture.allOf(accountsFutures.toArray(new CompletableFuture[accountsFutures.size()]));
                        }).thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                        .thenApply(res -> {
                                    List<Account> accountsList = new ArrayList();
                                    for (int i = 0; i < accountsFutures.size(); i++)
                                        accountsList.addAll(accountsFutures.get(i).join());

                                    return new CollectionPage<Account>(numberOfFolloewEntries[0], pageSize, page, accountsList);
                                }
                        );
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> createFollower(long accountToBeFollowedId, long accountToFollowId, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follows, Follows.FollowKey> followMapper = getMapper(Follows.class, unitOfWork);
        CompletableFuture<Void> future = accountService.getAccount(accountToFollowId, username)
                .thenCompose(account -> followMapper.create(new Follows(accountToBeFollowedId, accountToFollowId)))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteFollower(long id, long fid, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follows, Follows.FollowKey> followMapper = getMapper(Follows.class, unitOfWork);
        CompletableFuture<Void> future = accountService.getAccount(id, username)
                .thenCompose(account -> followMapper.deleteById(new Follows.FollowKey(id, fid)))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
