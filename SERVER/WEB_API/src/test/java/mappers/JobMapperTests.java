package mappers;

import dataBase.DataBaseTests;
import dataMapping.mappers.AccountMapper;
import dataMapping.mappers.JobMapper;
import dataMapping.utils.MapperRegistry;
import dataMapping.utils.UnitOfWork;
import model.Account;
import model.Job;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class JobMapperTests {
    private Connection con;

    @Before
    public void start() throws SQLException {
        con = DataBaseTests.getConnection();
        con.setAutoCommit(false);
        MapperRegistry.addEntry(Job.class, new JobMapper());
    }

    @After
    public void finish() throws SQLException {
        con.rollback();
        con.close();
    }

    @Test
    public void insertTest(){
        JobMapper jobMapper = (JobMapper) MapperRegistry.getMapper(Job.class);

        UnitOfWork.newCurrent();

        Job.create(1, null, 0, null, null, null, null, null, 0, null, null);

        UnitOfWork.getCurrent().commit();

        
    }
}
