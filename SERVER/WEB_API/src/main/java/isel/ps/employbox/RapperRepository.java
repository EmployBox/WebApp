package isel.ps.employbox;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface RapperRepository<T, K >{

  Optional<T> findById(K k);

  List<T> findAll();

  /*Finds all related entities from the Foreign Key Entitie */
  List<T> findByFK(Class ForeignKeyClass, long fk );

  void create(T t);

  void createAll(Iterable<T> t);

  void update(T t);

  void updateAll(Iterable<T> t);

  void deleteById(K k);

  void delete(T t);

  void deleteAll();

  void deleteAll(Iterable<K> keys);

}

