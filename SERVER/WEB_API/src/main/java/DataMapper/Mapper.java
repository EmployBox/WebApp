package DataMapper;

import Model.DomainObject;

public interface Mapper {
    void insert(DomainObject obj);
    void update(DomainObject obj);
    void delete(DomainObject obj);
}
