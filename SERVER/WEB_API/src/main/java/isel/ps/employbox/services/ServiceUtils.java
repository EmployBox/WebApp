package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
import io.vertx.ext.sql.TransactionIsolation;
import isel.ps.employbox.model.binders.CollectionPage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServiceUtils {

    private ServiceUtils() {
    }

    public static <T extends DomainObject<K>, K> CompletableFuture<CollectionPage<T>> getCollectionPageFuture(
            DataRepository<T, K> repo,
            int page,
            int pageSize,
            Pair<String, Object>... query
    ) {
        UnitOfWork unitOfWork = new UnitOfWork(TransactionIsolation.SERIALIZABLE);
        return repo.findWhere(unitOfWork, page, pageSize, query)
                .thenCompose(tList -> getCollectionPageCF(repo, page, pageSize, unitOfWork, tList, query));
    }

    private static <T extends DomainObject<K>, K> CompletableFuture<CollectionPage<T>> getCollectionPageCF(DataRepository<T, K> repo, int page, int pageSize,
                                                                                                           UnitOfWork unitOfWork, List<T> tList, Pair<String, Object>[] query) {
        return repo.getNumberOfEntries(unitOfWork, query)
                .thenCompose(aLong -> {
                    CollectionPage<T> collectionPage = new CollectionPage<>(aLong, pageSize, page, tList);
                    return unitOfWork.commit().thenApply(aVoid -> collectionPage);
                });
    }

    public static <T> CompletableFuture<T> handleExceptions(CompletableFuture<T> future, UnitOfWork unitOfWork) {
        return future.handleAsync((t, throwable) -> {
            if (throwable != null)
                return unitOfWork.rollback()
                        .thenApply(aVoid1 -> { throw (RuntimeException) throwable; })
                        .thenApply(o -> t); //thenApply used to avoid cast
            return CompletableFuture.completedFuture(t);
        })
                .thenCompose(voidCompletableFuture -> voidCompletableFuture);
    }
}
