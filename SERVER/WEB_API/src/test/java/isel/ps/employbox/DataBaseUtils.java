package isel.ps.employbox;

import com.github.jayield.rapper.utils.ConnectionManager;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;


public class DataBaseUtils {
    public static void prepareDB() throws SQLException {
        ConnectionManager connectionManager = ConnectionManager.getConnectionManager(
                "jdbc:hsqldb:file:" + URLDecoder.decode(DataBaseUtils.class.getClassLoader().getResource("testdb").getPath()) + "/testdb",
                "SA", "");
        //todo
        Connection con = null; //connectionManager.getConnection();
        con.prepareCall("{call deleteDB()}").execute();
        con.prepareCall("{call populateDB()}").execute();
        con.commit();
        con.close();
    }
}
