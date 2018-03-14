package isel.ps.EmployBox.dal.dataMapping;

import isel.ps.EmployBox.dal.domainModel.DomainObject;

public interface Mapper<T extends DomainObject<K>, K> {
    void insert(T obj);
    void update(T obj);
    void delete(T obj);
}
