package dataMapping.utils;

import com.microsoft.sqlserver.jdbc.SQLServerConnectionPoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.ConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.SQLException;

//TODO Apply a connection pool
public class ConnectionManager {
    private static ConnectionManager connectionManager = new ConnectionManager(null);

    public static ConnectionManager getConnectionManagerOfDefaultDB(){
        return connectionManager;
    }

    /**CONNECTION STRING FORMAT: SERVERNAME;DATABASE;USER;PASSWORD*/
    private final ConnectionPoolDataSource dataSource;

    public ConnectionManager (String envVarName){
        if(envVarName != null )
            dataSource = getDataSource(envVarName);
        else
            dataSource = getDataSource("DB_CONNECTION_STRING");
    }

    public Connection getConnection() {
        Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
        try {
            return dataSource
                    .getPooledConnection()
                    .getConnection();

        } catch (SQLException e) {
            logger.info("Error on stablishing connection to the DB \n" + e.getMessage());
        }
        return null;
    }

    private ConnectionPoolDataSource getDataSource(String envVar){
        String connectionString = System.getenv(envVar);
        String [] connectionStringParts = connectionString.split(";");


        SQLServerConnectionPoolDataSource dataSource = new SQLServerConnectionPoolDataSource();
        dataSource.setServerName(connectionStringParts[0]);
        dataSource.setDatabaseName(connectionStringParts[1]);
        dataSource.setUser(connectionStringParts[2]);
        dataSource.setPassword(connectionStringParts[3]);

        return dataSource;
    }
}
