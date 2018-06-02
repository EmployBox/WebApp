package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Follow;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class FollowService {
    private final DataRepository<Follow, Follow.FollowKey> followsRepo;
    private final AccountService accountService;

    public FollowService(DataRepository<Follow, Follow.FollowKey> followeRepo, AccountService accountRepo) {
        this.followsRepo = followeRepo;
        this.accountService = accountRepo;
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFollowers(long followedAccountId, int page) {
        return null;
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFollowing(long followerAccountId, int page) {
        return null;
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
