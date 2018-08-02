package isel.ps.employbox;


import com.github.jayield.rapper.connections.ConnectionManager;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import com.github.jayield.rapper.utils.SqlUtils;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.net.URLDecoder;

public class DataBaseUtils {
    public static void prepareDB() {
        ConnectionManager connectionManager = ConnectionManager.getConnectionManager(
                "jdbc:hsqldb:file:" + URLDecoder.decode(DataBaseUtils.class.getClassLoader().getResource("testdb").getPath()) + "/testdb",
                "SA",
                ""
        );

        UnitOfWork unit = new UnitOfWork(connectionManager::getConnection);
        SQLConnection con = unit.getConnection().join();
        SqlUtils.<ResultSet>callbackToPromise(ar -> con.call("{call deleteDB()}", ar)).join();
        SqlUtils.<ResultSet>callbackToPromise(ar -> con.call("{call populateDB()}", ar)).join();
        unit.commit().join();
    }
}
