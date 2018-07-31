package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Follow;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class FollowService {
    private final DataRepository<Follow, Follow.FollowKey> followsRepo;
    private final DataRepository<Account, Long> accountRepo;
    private final AccountService accountService;

    public FollowService(DataRepository<Follow, Follow.FollowKey> followeRepo, DataRepository<Account, Long> accountRepo1, AccountService accountRepo) {
        this.followsRepo = followeRepo;
        this.accountRepo = accountRepo1;
        this.accountService = accountRepo;
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFollowers(long followedAccountId, int page, int pageSize) {
        return getAccountFromFollowAux(followedAccountId, "accountIdFollower", page, pageSize);
    }

    public CompletableFuture<CollectionPage<Account>> getAccountFollowing(long followerAccountId, int page, int pageSize) {
        return getAccountFromFollowAux(followerAccountId, "accountIdFollowing", page, pageSize);
    }

    private CompletableFuture<CollectionPage<Account>> getAccountFromFollowAux(long followId, String collumn, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();

        CompletableFuture future = followsRepo.findWhere(unitOfWork, new Pair(collumn, followId))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenCompose(follow -> {
                    List<Pair<String, String>> pairs = new ArrayList<>();
                    ((List<Follow>) follow).forEach(curr -> pairs.add(new Pair("accountId", curr.getAccountIdFollowed())));
                    Pair[] query = pairs.stream()
                            .filter(stringStringPair -> stringStringPair.getValue() != null)
                            .toArray(Pair[]::new);
                    return ServiceUtils.getCollectionPageFuture(accountRepo, page, pageSize, query);
                });
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> createFollower(long accountToBeFollowedId, long accountToFollowId, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();

        CompletableFuture<Void> future = accountService.getAccount(accountToFollowId, username)
                .thenCompose(account -> followsRepo.create(unitOfWork, new Follow(accountToBeFollowedId, accountToFollowId)))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteFollower(long id, long fid, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();

        CompletableFuture<Void> future = accountService.getAccount(id, username)
                .thenCompose(account -> followsRepo.deleteById(unitOfWork, new Follow.FollowKey(id, fid)))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
