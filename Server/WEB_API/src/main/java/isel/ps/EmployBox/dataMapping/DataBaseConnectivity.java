package isel.ps.EmployBox.dataMapping;

import isel.ps.EmployBox.model.DomainObject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface DataBaseConnectivity<T extends DomainObject<K>, K> {
    Stream<T> executeSQLQuery(String query, Function<ResultSet, T> mapper, Consumer<PreparedStatement> prepareStatement);
    T executeSQLUpdate(String query, T obj, BiFunction<PreparedStatement, T, T> prepareStatement);
    T executeSQLProcedure(String call, T obj, BiFunction<CallableStatement, T, T> handleStatement);
}
