package isel.ps.employbox.model;

import java.util.List;

public interface ModelBinder<T, O, I, K> {
    List<O> bindOutput(List<T> list);
    List<T> bindInput(List<I> list);
    O bindOutput(T object);
    T bindInput(I object);
}
