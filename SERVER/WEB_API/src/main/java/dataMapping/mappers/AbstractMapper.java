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
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractMapper<T extends DomainObject> implements Mapper<T> {
    private final ConcurrentMap<Object, T> identityMap = new ConcurrentHashMap<>();

    public Map<Object, T> getIdentityMap() {
        return identityMap;
    }

    /**
     * Returns a string to findByPK a DomainObject by its ID
     * @return select query of the DomainObject
     */
    protected abstract String findByPKStatement();

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
                    throw new DataMapperException(e.getMessage(), e);
                }
            }
        }, false);
    }

    /**
     * Gets the object from Identity Map or queries the DB for it
     * @param primaryKey
     * @return the object queried
     * @throws SQLException
     */
    public Optional<T> findByPK(String primaryKey) throws DataMapperException {
        T result = getIdentityMap().get(primaryKey);
        if(result != null) return Optional.of(result);

        /*SQLServerDataSource dc = new SQLServerDataSource();
        Config config = new Config();
        dc.setDatabaseName(config.DB_NAME);
        dc.setUser(config.USER);
        dc.setPassword(config.PASSWORD);
        dc.setServerName(config.SERVER_NAME);
        return dc.getConnection();*/
        Connection con = ConnectionManager.getConnectionManager().getCon();
        /*con.setAutoCommit(false);
        if(con.getMetaData().supportsTransactionIsolationLevel(TRANSACTION_SERIALIZABLE))
            con.setTransactionIsolation(TRANSACTION_SERIALIZABLE);*/
        PreparedStatement statement;
        try {
            statement = con.prepareStatement(findByPKStatement());

            statement.setString(1, primaryKey);

            return stream(statement.executeQuery(), this::mapper).findFirst();
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    protected void DBHelper(String query, Function<PreparedStatement, SQLException> prepareStatement, Runnable handleIdentityMap){
        Connection conn = null;
        PreparedStatement stmt;
        try {
            //conn = ConnectionManager.INSTANCE.getConnection();
            stmt = conn.prepareStatement(query);

            SQLException exception = prepareStatement.apply(stmt);
            if(exception != null) throw exception;

            int rowCount = stmt.executeUpdate();
            if (rowCount == 0) {
                throw new ConcurrencyException("Concurrency problem found");
            }
            else {
                handleIdentityMap.run();
            }
        } catch (SQLException e) {
            throw new DataMapperException("unexpected error", e);
        }
    }
}
