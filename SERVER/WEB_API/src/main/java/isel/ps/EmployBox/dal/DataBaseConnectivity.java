package isel.ps.EmployBox.dal;

import isel.ps.EmployBox.dal.model.DomainObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DataBaseConnectivity<T extends DomainObject<K>, K> {
    CompletableFuture<List<T>> executeSQLQuery(String query, Function<ResultSet, T> mapper, Consumer<PreparedStatement> prepareStatement);
    CompletableFuture<T> executeSQLUpdate(String query, Function<? super Statement, T> handleStatement);
    CompletableFuture<T> executeSQLProcedure(String call, Function<? super Statement, T> handleStatement);
}
