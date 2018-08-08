package isel.ps.employbox.services;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import io.vertx.ext.sql.TransactionIsolation;
import isel.ps.employbox.model.binders.CollectionPage;

import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;

public class ServiceUtils {

    private ServiceUtils() {
    }

    public static <T extends DomainObject<K>, K> CompletableFuture<CollectionPage<T>> getCollectionPageFuture(
            Class<T> tClass,
            int page,
            int pageSize,
            Condition<Object>... query
    ) {
        UnitOfWork unitOfWork = new UnitOfWork(TransactionIsolation.SERIALIZABLE);
        DataMapper<T, K> mapper = getMapper(tClass, unitOfWork);

        CompletableFuture<CollectionPage<T>> future = mapper.find(page, pageSize, query)
                .thenCompose(tList -> mapper.getNumberOfEntries(query)
                        .thenCompose(aLong -> {
                            CollectionPage<T> collectionPage = new CollectionPage<>(aLong, pageSize, page, tList);
                            return unitOfWork.commit().thenApply(aVoid -> collectionPage);
                        }));

        return handleExceptions(future, unitOfWork);
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
