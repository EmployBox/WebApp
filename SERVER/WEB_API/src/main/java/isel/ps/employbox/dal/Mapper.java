package isel.ps.employbox.dal;

import isel.ps.employbox.dal.model.DomainObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Mapper<T extends DomainObject<K>, K> {
    CompletableFuture<T> getById(K id);
    CompletableFuture<List<T>> getAll();
    void insert(T obj);
    void update(T obj);
    void delete(T obj);
}
