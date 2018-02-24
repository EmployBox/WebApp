package model;

import dataMapper.UnitOfWork;

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

    /**
     * Query the DB for the object with ID passed in the parameters
     * @param id
     * @return the object queried
     * @throws SQLException
     */
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

    /**
     * Always called when creating new object
     */
    protected void markNew() {
        UnitOfWork.getCurrent().registerNew(this);
    }

    /**
     * Always called when reading an object from DB
     */
    protected void markClean() {
        UnitOfWork.getCurrent().registerClean(this);
    }

    /**
     * To be always called before making any changes to the object and calling markDirty()
     */
    protected void markToBeDirty(){
        UnitOfWork.getCurrent().registerClone(this);
    }

    /**
     * Always called when altering an object
     */
    protected void markDirty() {
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * Always called when removing an object
     */
    protected void markRemoved() {
        UnitOfWork.getCurrent().registerRemoved(this);
    }
}
