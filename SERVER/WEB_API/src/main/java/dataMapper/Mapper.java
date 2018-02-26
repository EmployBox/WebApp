package dataMapper;

import model.DomainObject;

public interface Mapper<T extends DomainObject> {
    void insert(T obj);
    void update(T obj);
    void delete(T obj);
}
