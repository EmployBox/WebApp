package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.Transaction;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Follow;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FollowService {
    private final DataRepository<Follow, Follow.FollowKey> followsRepo;
    private final AccountService accountService;

    public FollowService(DataRepository<Follow, Follow.FollowKey> followeRepo, AccountService accountRepo) {
        this.followsRepo = followeRepo;
        this.accountService = accountRepo;
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFollowers(long followedAccountId, int page, int pageSize) {
        return getAccountFutureByFollowType("accountIdFollower", followedAccountId, page, pageSize);
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFollowing(long followerAccountId, int page, int pageSize) {
        return getAccountFutureByFollowType("accountIdFollowing", followerAccountId, page, pageSize);
    }

    private CompletableFuture<CollectionPage<Account>> getAccountFutureByFollowType(String collum, long id, int page, int pageSize) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];

        return new Transaction(Connection.TRANSACTION_SERIALIZABLE)
                .andDo(() -> followsRepo.findWhere(page, pageSize, new Pair<>(collum, id))
                        .thenCompose(listRes -> {
                            list[0] = listRes;
                            return followsRepo.getNumberOfEntries(new Pair<>(collum, id));
                        })
                        .thenAccept(numberOfEntries -> ret[0] = new CollectionPage(
                                numberOfEntries,
                                pageSize,
                                page,
                                list[0]
                        ))
                ).commit()
                .thenApply(__ -> ret[0]);
    }

    public Mono<Void> createFollower(long accountToBeFollowedId, long accountToFollowId, String username) {
        return Mono.fromFuture(
                accountService.getAccount(accountToFollowId, username)
                        .thenCompose(account -> followsRepo.create(new Follow(accountToBeFollowedId, accountToFollowId)))
        );
    }

    public Mono<Void> deleteFollower(long id, long fid, String username) {
        return Mono.fromFuture(
                accountService.getAccount(id, username)
                        .thenCompose(account -> followsRepo.deleteById(new Follow.FollowKey(id, fid)))
        );
    }
}
