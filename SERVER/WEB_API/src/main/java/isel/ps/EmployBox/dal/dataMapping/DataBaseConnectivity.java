package isel.ps.EmployBox.dal.dataMapping;

import isel.ps.EmployBox.dal.domainModel.DomainObject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface DataBaseConnectivity<T extends DomainObject<K>, K> {
    CompletableFuture<List<T>> executeSQLQuery(String query, Function<ResultSet, T> mapper, Consumer<PreparedStatement> prepareStatement);
    CompletableFuture<T> executeSQLUpdate(String query, Function<Statement, T> prepareStatement);
    CompletableFuture<T> executeSQLProcedure(String call, Function<Statement, T> handleStatement);
}
