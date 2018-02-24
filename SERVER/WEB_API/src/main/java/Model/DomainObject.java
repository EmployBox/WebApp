package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract class DomainObject {
    public abstract Map<String, DomainObject> getIdentityMap();
    public abstract String getId();

    /**
     * Returns a string to find a DomainObject by its ID
     * @return select query of the DomainObject
     */
    public abstract String findStatement();

    /**
     * Inserts the objects read into the LoadedMap
     * @param rs - ResultSet with the result of the DB
     */
    public abstract DomainObject load(ResultSet rs);

    public DomainObject find(String id) throws SQLException {
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

        DomainObject result = getIdentityMap().get(id);
        if(result != null) return result;
        PreparedStatement statement = con.prepareStatement(findStatement());
        statement.setString(1, id);

        return load(statement.executeQuery());
    }
}
