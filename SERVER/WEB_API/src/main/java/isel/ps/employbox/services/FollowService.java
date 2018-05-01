package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Follow;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class FollowService {
    private final DataRepository<Follow, Follow.FollowKey> followsRepo;
    private final AccountService accountService;

    public FollowService(DataRepository<Follow, Follow.FollowKey> followeRepo, AccountService accountRepo) {
        this.followsRepo = followeRepo;
        this.accountService = accountRepo;
    }

    public CompletableFuture<Stream<Account>> getAccountFollowers(long followedAccountId) {
        ArrayList<CompletableFuture<Account>> arr = new ArrayList<>();
        return followsRepo.findWhere(new Pair<>("accountIdFollowed", followedAccountId))
                .thenAccept(accounts -> accounts
                        .forEach(elem -> arr.add(accountService.getAccount(elem.getAccountIdFollower()))))
                .thenCompose(__ -> CompletableFuture.allOf(arr.toArray(new CompletableFuture[arr.size()])))
                .thenApply(__ -> arr.stream().map(CompletableFuture::join));
    }

    public CompletableFuture<Stream<Account>> getAccountFollowing(long followerAccountId) {
        ArrayList<CompletableFuture<Account>> arr = new ArrayList<>();
        return followsRepo.findWhere(new Pair<>("accountIdFollower", followerAccountId))
                .thenAccept(accounts -> accounts
                        .forEach(elem -> arr.add(accountService.getAccount(elem.getAccountIdFollowed()))))
                .thenCompose(__ -> CompletableFuture.allOf(arr.toArray(new CompletableFuture[arr.size()])))
                .thenApply(__ -> arr.stream().map(CompletableFuture::join));
    }

    public Mono<Void> createFollower(long accountToBeFollowedId, long accountToFollowId, String username) {
        return Mono.fromFuture(
                accountService.getAccount(accountToFollowId, username)
                        .thenCompose(
                                __ -> followsRepo.create(new Follow(accountToBeFollowedId, accountToFollowId))
                                        .thenAccept(res -> {
                                            if (!res)
                                                throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                                        }))
        );
    }

    public Mono<Void> deleteFollower(long id, long fid, String username) {
        return Mono.fromFuture(
                accountService.getAccount(id, username)
                        .thenCompose(__ -> followsRepo.deleteById(new Follow.FollowKey(id, fid)))
                        .thenAccept(res -> {
                            if (!res)
                                throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                        })
        );
    }
}
