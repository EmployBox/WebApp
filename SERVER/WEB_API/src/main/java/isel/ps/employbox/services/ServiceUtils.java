package isel.ps.employbox.services;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import com.github.jayield.rapper.utils.Pair;
import io.vertx.ext.sql.TransactionIsolation;
import isel.ps.employbox.model.binders.CollectionPage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;

public class ServiceUtils {

    private ServiceUtils() {
    }

    public static <T extends DomainObject<K>, K> CompletableFuture<CollectionPage<T>> getCollectionPageFuture(
            Class<T> tClass,
            int page,
            int pageSize,
            Pair<String, Object>... query
    ) {
        UnitOfWork unitOfWork = new UnitOfWork(TransactionIsolation.SERIALIZABLE);
        DataMapper<T, K> mapper = getMapper(tClass, unitOfWork);

        return handleExceptions(mapper.findWhere(page, pageSize, query)
                .thenCompose(tList -> getCollectionPageCF(mapper, page, pageSize, unitOfWork, tList, query)), unitOfWork);
    }

    private static <T extends DomainObject<K>, K> CompletableFuture<CollectionPage<T>> getCollectionPageCF(DataMapper<T, K> repo, int page, int pageSize,
                                                                                                           UnitOfWork unitOfWork, List<T> tList, Pair<String, Object>[] query) {
        return repo.getNumberOfEntries(query)
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
