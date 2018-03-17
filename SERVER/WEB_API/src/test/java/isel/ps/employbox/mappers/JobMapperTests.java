package isel.ps.employbox.mappers;

import isel.ps.employbox.DataBaseTests;
import isel.ps.employbox.dal.mappers.JobMapper;
import isel.ps.employbox.dal.util.MapperRegistry;
import isel.ps.employbox.dal.util.UnitOfWork;
import isel.ps.employbox.dal.model.Job;
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

        //UnitOfWork.newCurrent();



        UnitOfWork.getCurrent().commit();
    }
}
