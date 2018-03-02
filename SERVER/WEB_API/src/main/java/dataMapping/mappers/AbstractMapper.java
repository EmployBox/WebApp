package dataMapping.mappers;

import dataMapping.Mapper;
import dataMapping.exceptions.ConcurrencyException;
import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.ConnectionManager;
import model.DomainObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public abstract class AbstractMapper<T extends DomainObject<K>, K> implements Mapper<T, K> {

    private final ConcurrentMap<K, T> identityMap = new ConcurrentHashMap<>();

    public Map<K, T> getIdentityMap() {
        return identityMap;
    }

    /**
     * Converts the current row from result set into an object
     * @param rs
     * @return DomainObject
     * @throws DataMapperException
     */
    abstract T mapper(ResultSet rs) throws DataMapperException;

    /**
     * Inserts the objects read into the LoadedMap
     * @param rs - ResultSet with the result of the DB
     */
    Stream<T> stream(ResultSet rs, Function<ResultSet, T> func) throws DataMapperException{
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
        }, false).onClose(() -> { try { rs.close(); } catch (SQLException e) { throw new DataMapperException(e.getMessage(), e); } });
    }

    private<R> R executeSQLAux(String query, Function<PreparedStatement, SQLException> prepareStatement, Function<PreparedStatement, R> function){
        Connection con = ConnectionManager.getConnectionManager().getConnection();
        try(PreparedStatement statement = con.prepareStatement(query)) {
            SQLException exception = prepareStatement.apply(statement);
            if(exception != null) throw new DataMapperException(exception.getMessage(), exception);

            return function.apply(statement);
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
        //TODO Alert ConnectionManager we finished using conn
    }

    /**
     * Gets the object from IdentityMap or queries the DB for it
     * @param query
     * @param key
     * @param prepareStatement
     * @return
     */

    public Stream<T> executeQuery(String query, K key, Function<PreparedStatement, SQLException> prepareStatement){
        Map<K, T> identityMap = getIdentityMap();
        if(identityMap.containsKey(key))
            return Stream.of(identityMap.get(key));

        return executeSQLAux(query, prepareStatement, statement -> {
            try {
                return stream(statement.executeQuery(), this::mapper);
            } catch (SQLException e) {
                throw new DataMapperException(e.getMessage(), e);
            }
        });
    }

    /**
     * Executes a sql update and saves the obj in the IdentityMap if successful else throws ConcurrencyException
     * @param query
     * @param obj
     * @param prepareStatement
     */
    protected void executeSQLUpdate(String query, T obj, Function<PreparedStatement, SQLException> prepareStatement){
        executeSQLAux(query, prepareStatement, statement -> {
            try{
                int rowCount = statement.executeUpdate();
                if (rowCount == 0) throw new ConcurrencyException("Concurrency problem found");
                else {
                    if (obj.getIdentityKey() != obj.getDefaultKey()) getIdentityMap().put(obj.getIdentityKey(), obj);
                    else {
                        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                            if (generatedKeys.next()) return null;//TODO obj.setIdentityKey(generatedKeys.getLong(1));

                            else throw new DataMapperException("Creating user failed, no ID obtained.", null);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new DataMapperException(e.getMessage(), e);
            }
            return null;
        });
    }
}
