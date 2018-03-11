package dataMapping.utils;

import dataMapping.exceptions.ConcurrencyException;
import dataMapping.exceptions.DataMapperException;
import model.DomainObject;

import java.sql.*;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SQLUtils {

    /**
     * Inserts the objects read into the LoadedMap
     * @param rs - ResultSet with the result of the DB
     */
    private static<T> Stream<T> stream(Statement statement, ResultSet rs, Function<ResultSet, T> func) throws DataMapperException{
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

    private static void handleSQLStatement(String query, boolean isProcedure, Consumer<Statement> handleStatement){
        Connection connection = UnitOfWork.getCurrent().getConnection();
        try {
            Statement statement = isProcedure ? connection.prepareCall(query) : connection.prepareStatement(query);
            handleStatement.accept(statement);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    public static<T> void executeSQLProcedure(String call, T obj, BiConsumer<CallableStatement, T> handleStatement){
        handleSQLStatement(
                call,
                true,
                statement -> handleStatement.accept((CallableStatement) statement, obj)
        );
    }

    public static<T> Stream<T> executeSQLQuery(String query, Function<ResultSet, T> mapper, Consumer<PreparedStatement> prepareStatement){
        Object[] result = new Object[1];
        handleSQLStatement(
                query,
                false,
                statement -> {
                    prepareStatement.accept((PreparedStatement) statement);
                    try {
                        result[0] = stream(statement, ((PreparedStatement) statement).executeQuery(), mapper);
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
        return (Stream<T>) result[0];
    }

    /**
     * Executes a sql update and saves the obj in the IdentityMap if successful else throws ConcurrencyException
     * @param query
     * @param obj
     * @param prepareStatement
     */
    public static<T> void executeSQLUpdate(String query, T obj, BiConsumer<PreparedStatement, T> prepareStatement){
        handleSQLStatement(query,
                false,
                statement -> {
                    prepareStatement.accept((PreparedStatement) statement, obj);
                    try{
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
                    }
                }
        );
    }
}
