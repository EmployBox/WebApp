package dataMapper;

import model.DomainObject;

public interface Mapper<T extends DomainObject> {
    void insert(T obj) throws DataMapperException;
    void update(T obj) throws DataMapperException, ConcurrencyException;
    void delete(T obj) throws DataMapperException, ConcurrencyException;
}
