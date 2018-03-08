package mappers;

import dataBase.DataBaseTests;
import dataMapping.mappers.JobMapper;
import dataMapping.utils.MapperRegistry;
import dataMapping.utils.UnitOfWork;
import model.Job;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

//todo
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
    public void insertTest() throws SQLException {
        JobMapper jobMapper = (JobMapper) MapperRegistry.getMapper(Job.class);

        UnitOfWork.newCurrent();



        UnitOfWork.getCurrent().commit();
    }
}
