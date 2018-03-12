package isel.ps.EmployBox.dataMapping.utils;

import com.microsoft.sqlserver.jdbc.SQLServerConnectionPoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.ConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    public static final String defaultDB = "DB_CONNECTION_STRING";
    public static final String testDB = "DBTEST_CONNECTION_STRING";
    private static ConnectionManager connectionManager = null;

    //TODO String -> Enum
    public static ConnectionManager getConnectionManager(String envVar){
        if(connectionManager == null) connectionManager = new ConnectionManager(envVar);
        return connectionManager;
    }

    /**CONNECTION STRING FORMAT: SERVERNAME;DATABASE;USER;PASSWORD*/
    private final ConnectionPoolDataSource dataSource;

    public ConnectionManager (String envVarName){
        dataSource = getDataSource(envVarName);
    }

    public Connection getConnection() {
        try {
            Connection connection = dataSource
                    .getPooledConnection()
                    .getConnection();
            connection.setAutoCommit(false);
            return connection;
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
