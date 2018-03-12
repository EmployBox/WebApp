package isel.ps.EmployBox.dataMapping;

import isel.ps.EmployBox.model.DomainObject;

public interface Mapper<T extends DomainObject<K>, K> {
    void insert(T obj);
    void update(T obj);
    void delete(T obj);
}
