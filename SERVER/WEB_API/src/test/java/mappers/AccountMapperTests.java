package mappers;

import dataBase.DataBaseTests;
import dataMapping.mappers.AccountMapper;
import dataMapping.utils.MapperRegistry;
import dataMapping.utils.UnitOfWork;
import model.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

public class AccountMapperTests {

    private Connection con;

    @Before
    public void start() throws SQLException {
        con = DataBaseTests.getConnection();
        con.setAutoCommit(false);
        MapperRegistry.addEntry(Account.class, new AccountMapper());
    }

    @After
    public void finish() throws SQLException {
        con.rollback();
        con.close();
    }

    @Test
    public void insertTest(){
        AccountMapper mapper = (AccountMapper) MapperRegistry.getMapper(Account.class);

        UnitOfWork.newCurrent();
        UnitOfWork unit = UnitOfWork.getCurrent();
        Account.create("Test@gmail.com", "1234", 0);
        unit.commit();

        Optional<Account> accountOptional = mapper.findByEmail("Test@gmail.com");
        assertTrue(accountOptional.isPresent());
    }
}
