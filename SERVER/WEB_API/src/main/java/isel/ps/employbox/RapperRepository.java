package isel.ps.employbox;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public interface RapperRepository<T, K> {
    CompletableFuture<Optional<T>> findById(K k);
    CompletableFuture<List<T>> findAll();
    CompletableFuture create(T t);
    CompletableFuture createAll(Iterable<T> t);
    CompletableFuture update(T t);
    CompletableFuture updateAll(Iterable<T> t);
    CompletableFuture deleteById(K k);
    CompletableFuture delete(T t);
    CompletableFuture deleteAll(Iterable<K> keys);
}

