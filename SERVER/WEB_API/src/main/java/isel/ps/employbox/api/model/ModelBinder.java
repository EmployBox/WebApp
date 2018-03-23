package isel.ps.employbox.api.model;

import isel.ps.employbox.dal.model.DomainObject;

import java.util.List;

public interface ModelBinder<T extends DomainObject<K>, O, I, K> {
    List<O> bindOutput(List<T> list);
    List<T> bindInput(List<I> list);
    O bindOutput(T object);
    T bindInput(I object);
}
