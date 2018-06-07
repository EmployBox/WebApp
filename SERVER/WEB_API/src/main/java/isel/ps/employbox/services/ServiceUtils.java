package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Transaction;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.model.binder.CollectionPage;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServiceUtils {
    public static <T extends DomainObject<K>, K> CompletableFuture<CollectionPage<T>> getCollectionPageFuture(
            DataRepository<T, K> repo,
            int page,
            int pageSize,
            Pair<String, Long>... query
    ) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];

        return new Transaction(Connection.TRANSACTION_SERIALIZABLE)
                .andDo(() ->
                        repo.findWhere(page, pageSize, query)
                                .thenCompose(listRes -> {
                                    list[0] = listRes;
                                    return repo.getNumberOfEntries(query);
                                })
                                .thenAccept(numberOfEntries -> ret[0] = new CollectionPage(
                                        numberOfEntries,
                                        pageSize,
                                        page,
                                        list[0]
                                ))
                ).commit()
                .thenApply(__ -> ret[0]);
    }
}
