package mappers;

import dataBase.DataBaseTests;
import dataMapping.mappers.AccountMapper;
import dataMapping.mappers.UserMapper;
import dataMapping.utils.MapperRegistry;
import dataMapping.utils.UnitOfWork;
import model.Account;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;


public class UserMapperTests {

    private Connection con;

    @Before
    public void start() throws SQLException {
        con = DataBaseTests.getConnection();
        con.setAutoCommit(false);
        MapperRegistry.addEntry(User.class, new UserMapper());
    }

    @After
    public void finish() throws SQLException {
        con.rollback();
        con.close();
    }

    @Test
    public void insertTest() throws SQLException {
        UserMapper mapper = (UserMapper) MapperRegistry.getMapper(User.class);

        UnitOfWork.newCurrent();

        User user = User.create("Test@gmail.com", "1234", 0, "Manel", "Sou um espetaculo", "someurl", null, null, null, null, null, null, null);

        UnitOfWork.getCurrent().commit();
    }
}
