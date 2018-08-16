package isel.ps.employbox.services;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.OrderCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import io.vertx.ext.sql.TransactionIsolation;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.CollectionPage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        //removes order by clauses from numberOfEntriesQuery to avoid possible SQL errors
        List<Condition<Object>> numberOfEntriesQuery = Stream.of(query).filter(curr -> !curr.getClass().isAssignableFrom(OrderCondition.class)).collect(Collectors.toList());

        CompletableFuture<CollectionPage<T>> future = mapper.find(page, pageSize, query)
                .thenCompose(tList -> mapper.getNumberOfEntries(numberOfEntriesQuery.toArray(new Condition[numberOfEntriesQuery.size()]))
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

    public static void evaluateOrderClause(String orderColumn, String orderClause, List<Condition> conditionPairs) {
        if(orderColumn != null) {
            if (!(orderClause.compareTo("ASC") == 0 || orderClause.compareTo("DESC") == 0))
                throw new BadRequestException("clause is not equal to ASC or DESC");
            if (orderClause.compareTo("ASC") == 0)
                conditionPairs.add(OrderCondition.asc(orderColumn));
            conditionPairs.add(OrderCondition.desc(orderColumn));
        }
    }
}
