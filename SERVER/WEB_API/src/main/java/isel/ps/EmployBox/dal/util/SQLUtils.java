package isel.ps.EmployBox.dal.util;

import isel.ps.EmployBox.dal.DataBaseConnectivity;

import isel.ps.EmployBox.dal.exceptions.ConcurrencyException;
import isel.ps.EmployBox.dal.exceptions.DataMapperException;
import isel.ps.EmployBox.dal.model.DomainObject;



import java.sql.*;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

import java.util.concurrent.CompletableFuture;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SQLUtils<T extends DomainObject<K>, K> implements DataBaseConnectivity<T, K> {

    public static void executeUpdate(PreparedStatement statement) throws SQLException {
        int rowCount = statement.executeUpdate();
        if (rowCount == 0) throw new ConcurrencyException("Concurrency problem found");
    }

    public static long getVersion(PreparedStatement statement) throws SQLException {
        long version;
        try (ResultSet inserted = statement.getResultSet()) {
            if (inserted.next()){
                version = inserted.getLong(1);
            }
            else throw new DataMapperException("Error inserting new entry");
        }
        return version;
    }

    public static long getGeneratedKey(PreparedStatement preparedStatement) throws SQLException {
        long jobId;
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()){
                jobId = generatedKeys.getLong(1);
            }
            else throw new DataMapperException("Error inserting new entry");
        }
        return jobId;
    }

    /**
     * Inserts the objects read into the LoadedMap
     * @param rs - ResultSet with the result of the DB
     */
    private Stream<T> stream(Statement statement, ResultSet rs, Function<ResultSet, T> func) throws DataMapperException{
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(
                Long.MAX_VALUE, Spliterator.ORDERED) {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                try {
                    if(!rs.next())return false;
                    action.accept(func.apply(rs));
                    return true;
                } catch (SQLException e) {
                    throw new DataMapperException(e);
                }
            }
        }, false).onClose(() -> {
            try {
                rs.close();
                statement.close();
            } catch (SQLException e) {
                throw new DataMapperException(e.getMessage(), e);
            }
        });
    }

    private<U> CompletableFuture<U> handleSQLStatement(String query, boolean isProcedure, Function<Statement, U> handleStatement){
        Connection connection = UnitOfWork.getCurrent().getConnection();
        try {
            Statement statement = isProcedure ? connection.prepareCall(query) : connection.prepareStatement(query);
            return CompletableFuture.supplyAsync(()->handleStatement.apply(statement));
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    @Override
    public CompletableFuture<T> executeSQLProcedure(String call, Function<? super Statement, T> handleStatement){
        return handleSQLStatement(
                call,
                true,
                handleStatement::apply
        );
    }

    @Override
    public CompletableFuture<List<T>> executeSQLQuery(String query, Function<ResultSet, T> mapper, Consumer<PreparedStatement> prepareStatement){
        return handleSQLStatement(
                query,
                false,
                statement -> {
                    prepareStatement.accept((PreparedStatement) statement);
                    try {
                        return stream(statement, ((PreparedStatement) statement).executeQuery(), mapper);
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        ).thenApply(s-> s.collect(Collectors.toList()));
    }

    /**
     * Executes a sql update and saves the obj in the IdentityMap if successful else throws ConcurrencyException
     * @param query
     * @param prepareStatement
     */
    @Override
    public CompletableFuture<T> executeSQLUpdate(String query, Function<? super Statement, T> prepareStatement){
        return handleSQLStatement(query,
                false,
                prepareStatement::apply
        );
    }
}
