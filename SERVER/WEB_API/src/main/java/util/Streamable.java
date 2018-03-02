package util;

import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
public interface Streamable<T> extends Supplier<Stream<T>>{}
