package isel.ps.employbox.dal;

import java.util.List;
import java.util.Optional;

public interface RapperRepository<T, K> {

    Optional<T> findById(K k);

    List<T> findAll();

    void create(T t);

    void createAll(Iterable<T> t);

    void update(T t);

    void updateAll(Iterable<T> t);

    void deleteById(K k);

    void delete(T t);

    void deleteAll();

    void deleteAll(Iterable<K> keys);

}
