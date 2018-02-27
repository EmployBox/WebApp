import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class DataBaseTests {

    public static Connection getConnection() throws SQLServerException {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setDatabaseName("PS_TEST_API_DATABASE");
        dataSource.setServerName("localhost");
        dataSource.setUser("PSG15");
        dataSource.setPassword("projectoSeminario");
        return dataSource.getConnection();
    }

    private Connection con;

    @Before
    public void start() throws SQLException {
        con = getConnection();
        con.setAutoCommit(false);
    }

    @After
    public void finish() throws SQLException {
        con.rollback();
        con.close();
    }

    @Test
    public void CRUDTest() throws SQLException {
        PreparedStatement statement = con.prepareStatement("INSERT INTO Experience (Competence, Years) Values (?, ?)");
        statement.setString(1, "SQL Server");
        statement.setShort(2, (short) 2);
        statement.executeUpdate();

        statement = con.prepareStatement("SELECT * FROM Experience WHERE competence = ?");
        statement.setString(1, "SQL Server");
        ResultSet rs = statement.executeQuery();

        assertTrue(rs.next());
        assertEquals((short) 2, rs.getShort("years"));
        long experienceId = rs.getLong("experienceId");

        statement = con.prepareStatement("UPDATE Experience SET years = 1 WHERE experienceId = ?");
        statement.setLong(1, experienceId);
        int rows = statement.executeUpdate();

        assertEquals(1, rows);

        statement = con.prepareStatement("DELETE FROM Experience WHERE experienceId = ?");
        statement.setLong(1, experienceId);
        rows = statement.executeUpdate();

        assertEquals(1, rows);
    }
}
