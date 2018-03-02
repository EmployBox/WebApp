package dataMapping;

import model.DomainObject;

public interface Mapper<T extends DomainObject<K>, K> {
    void insert(T obj);
    void update(T obj);
    void delete(T obj);
}
