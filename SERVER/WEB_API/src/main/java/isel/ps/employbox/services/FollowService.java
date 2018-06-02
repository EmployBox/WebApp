package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
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
        return accountService.getAccount(followedAccountId)
                .thenCompose(Account::getFollowers)
                .thenApply(list -> new CollectionPage(
                        list.size(),
                        page,
                        list.stream()
                                .skip(CollectionPage.PAGE_SIZE * page)
                                .limit(CollectionPage.PAGE_SIZE))
                );
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFollowing(long followerAccountId, int page) {
        return accountService.getAccount(followerAccountId)
                .thenCompose(Account::getFollowing)
                .thenApply(list -> new CollectionPage(
                        list.size(),
                        page,
                        list.stream()
                                .skip(CollectionPage.PAGE_SIZE * page)
                                .limit(CollectionPage.PAGE_SIZE))
                );
    }

    public Mono<Void> createFollower(long accountToBeFollowedId, long accountToFollowId, String username) {
        return Mono.fromFuture(
                accountService.getAccount(accountToFollowId, username)
                        .thenCompose(
                                __ -> followsRepo.create(new Follow(accountToBeFollowedId, accountToFollowId))
                                        .thenAccept(res -> {
                                            if (res.isPresent())
                                                throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                                        }))
        );
    }

    public Mono<Void> deleteFollower(long id, long fid, String username) {
        return Mono.fromFuture(
                accountService.getAccount(id, username)
                        .thenCompose(__ -> followsRepo.deleteById(new Follow.FollowKey(id, fid)))
                        .thenAccept(res -> {
                            if (res.isPresent())
                                throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                        })
        );
    }
}
