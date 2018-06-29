package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
import io.vertx.ext.sql.TransactionIsolation;
import isel.ps.employbox.model.binders.CollectionPage;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServiceUtils {

    private ServiceUtils() {
    }

    public static <T extends DomainObject<K>, K> CompletableFuture<CollectionPage<T>> getCollectionPageFuture(
            DataRepository<T, K> repo,
            int page,
            int pageSize,
            Pair ... query
    ) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];
        UnitOfWork unitOfWork = new UnitOfWork(TransactionIsolation.SERIALIZABLE);
        return repo.findWhere(unitOfWork, page, pageSize, query)
                .thenCompose(listRes -> {
                    list[0] = (List) listRes;
                    return repo.getNumberOfEntries(unitOfWork, query);
                })
                .thenAccept(numberOfEntries -> ret[0] = new CollectionPage(
                        (Long) numberOfEntries,
                        pageSize,
                        page,
                        list[0]
                )).thenCompose( __ -> unitOfWork.commit().thenApply(aVoid -> ret[0]));
    }
}
