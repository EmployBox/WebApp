package isel.ps.EmployBox.mappers;

import isel.ps.EmployBox.DataBaseTests;
import isel.ps.EmployBox.dal.dataMapping.mappers.JobMapper;
import isel.ps.EmployBox.dal.dataMapping.utils.MapperRegistry;
import isel.ps.EmployBox.dal.dataMapping.utils.UnitOfWork;
import isel.ps.EmployBox.dal.domainModel.Job;
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