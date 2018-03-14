package isel.ps.EmployBox.dal.dataMapping.utils;

import isel.ps.EmployBox.dal.dataMapping.DataBaseConnectivity;

import isel.ps.EmployBox.dal.dataMapping.exceptions.DataMapperException;
import isel.ps.EmployBox.dal.domainModel.DomainObject;


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

         /*try{
            int rowCount = ((PreparedStatement) statement).executeUpdate();
            if (rowCount == 0) throw new ConcurrencyException("Concurrency problem found");

            long generatedKey = 0;
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()){
                    generatedKey = generatedKeys.getLong(1);
                }
            }


        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }*/
    }
}
