package isel.ps.employbox.services;


import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Follow;
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

    public CompletableFuture<CollectionPage<Account>> getAccountFollowing(long followerAccountId, int page, int pageSize) {
        return getAccountFromFollowAux(followerAccountId, "accountIdFollowing", page, pageSize);
    }

    private CompletableFuture<CollectionPage<Account>> getAccountFromFollowAux(long followId, String collumn, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follow, Follow.FollowKey> followMapper = getMapper(Follow.class, unitOfWork);
        CompletableFuture future = followMapper.findWhere(new Pair(collumn, followId))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenCompose(follow -> {
                    List<Pair<String, String>> pairs = new ArrayList<>();
                    ((List<Follow>) follow).forEach(curr -> pairs.add(new Pair("accountId", curr.getAccountIdFollowed())));
                    Pair[] query = pairs.stream()
                            .filter(stringStringPair -> stringStringPair.getValue() != null)
                            .toArray(Pair[]::new);
                    return ServiceUtils.getCollectionPageFuture(Account.class, page, pageSize, query);
                });
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> createFollower(long accountToBeFollowedId, long accountToFollowId, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follow, Follow.FollowKey> followMapper = getMapper(Follow.class, unitOfWork);
        CompletableFuture<Void> future = accountService.getAccount(accountToFollowId, username)
                .thenCompose(account -> followMapper.create(new Follow(accountToBeFollowedId, accountToFollowId)))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteFollower(long id, long fid, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Follow, Follow.FollowKey> followMapper = getMapper(Follow.class, unitOfWork);
        CompletableFuture<Void> future = accountService.getAccount(id, username)
                .thenCompose(account -> followMapper.deleteById(new Follow.FollowKey(id, fid)))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
