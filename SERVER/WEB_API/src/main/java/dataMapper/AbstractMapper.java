package dataMapper;

import model.DomainObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractMapper<T extends DomainObject> implements Mapper<T> {
    private final ConcurrentMap<String, T> identityMap;

    public AbstractMapper(ConcurrentMap<String, T> identityMap) {
        this.identityMap = identityMap;
    }

    public Map<String, T> getIdentityMap() {
        return identityMap;
    }

    /**
     * Returns a string to findByPK a DomainObject by its ID
     * @return select query of the DomainObject
     */
    protected abstract String findByPKStatement();

    /**
     * Converts the current row from result set into an object
     * @param set
     * @return DomainObject
     * @throws DataMapperException
     */
    protected abstract T mapper(ResultSet set) throws DataMapperException;

    /**
     * Inserts the objects read into the LoadedMap
     * @param rs - ResultSet with the result of the DB
     */
    private Stream<T> stream(ResultSet rs, Function<ResultSet, T> func) throws DataMapperException{
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
        Connection con = null;
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
}
