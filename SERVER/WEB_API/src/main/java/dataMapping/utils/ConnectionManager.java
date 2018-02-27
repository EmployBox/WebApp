package dataMapping.utils;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
//TODO Apply a connection pool
public class ConnectionManager {

    private Connection con;

    public ConnectionManager() {

        Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setDatabaseName("PS_TEST_API_DATABASE");
        dataSource.setServerName("localhost");
        dataSource.setUser("PSG15");
        dataSource.setPassword("projectoSeminario");
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);
        } catch (SQLException e) {
            logger.info("Error on stablishing connection to the DB \n" + e.getMessage());
        }
    }

    public Connection getCon() {
        return con;
    }

    private static ConnectionManager connectionManager = new ConnectionManager();

    public static ConnectionManager getConnectionManager(){
        return connectionManager;
    }
}
