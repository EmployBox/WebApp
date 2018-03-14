package isel.ps.EmployBox.util;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
public interface Streamable<T> extends Supplier<List<T>>{}
