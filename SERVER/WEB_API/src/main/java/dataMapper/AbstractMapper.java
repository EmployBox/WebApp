package dataMapper;

import model.DomainObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractMapper<T extends DomainObject> implements Mapper<T> {
    private final Map<String, T> identityMap;

    public AbstractMapper(Map<String, T> identityMap) {
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
     * Inserts the objects read into the LoadedMap
     * @param rs - ResultSet with the result of the DB
     */
    protected abstract Optional<T> load(ResultSet rs) throws SQLException;

    /**
     * Gets the object from Identity Map or queries the DB for it
     * @param primaryKey
     * @return the object queried
     * @throws SQLException
     */
    public Optional<T> findByPK(String primaryKey) throws SQLException {
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
        PreparedStatement statement = con.prepareStatement(findByPKStatement());
        statement.setString(1, primaryKey);

        return load(statement.executeQuery());
    }
}
