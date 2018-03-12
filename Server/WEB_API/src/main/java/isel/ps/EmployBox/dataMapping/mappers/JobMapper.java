package isel.ps.EmployBox.dataMapping.mappers;

import isel.ps.EmployBox.dataMapping.exceptions.DataMapperException;
import isel.ps.EmployBox.dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import isel.ps.EmployBox.model.Application;
import isel.ps.EmployBox.model.Job;
import isel.ps.EmployBox.model.JobExperience;
import isel.ps.EmployBox.util.Streamable;

import java.sql.*;

import static isel.ps.EmployBox.dataMapping.utils.MapperRegistry.getMapper;

public class JobMapper extends AbstractMapper<Job, Long> {
    public JobMapper() {
        super(
                Job.class,
                PreparedStatement.class,
                JobMapper::prepareInsertStatement,
                JobMapper::prepareInsertStatement,
                JobMapper::prepareInsertStatement
        );
    }

    public Streamable<Job> findForAccount(long accountID){
        return findWhere(new Pair<>("accountId", accountID));
    }

    @Override
    public Job mapper(ResultSet rs) throws DataMapperException {
        try {
            long jobID = rs.getLong("JobID");
            long accountID = rs.getLong("AccountID");
            String address = rs.getString("Address");
            int wage = rs.getInt("Wage");
            String description = rs.getString("Description");
            String schedule = rs.getString("Schedule");
            Date offerBeginDate = rs.getDate("OfferBeginDate");
            Date offerEndDate = rs.getDate("OfferEndDate");
            String offerType = rs.getString("OfferType");
            long version = rs.getLong("Version");

            Streamable<Application> applications = ((ApplicationMapper) getMapper(Application.class)).findJobApplications(jobID);
            Streamable<JobExperience> jobExperiences = ((JobExperienceMapper) getMapper(JobExperience.class)).findExperiences(jobID);

            Job job = Job.load(jobID, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version, applications, jobExperiences);
            identityMap.put(jobID, job);

            return job;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    private static void prepareInsertStatement(PreparedStatement preparedStatement, Job obj){
        try{
            preparedStatement.setLong(1, obj.getAccountID());
            preparedStatement.setString(2, obj.getAddress());
            preparedStatement.setInt(3, obj.getWage());
            preparedStatement.setString(4, obj.getDescription());
            preparedStatement.setString(5, obj.getSchedule());
            preparedStatement.setDate(6, obj.getOfferBeginDate());
            preparedStatement.setDate(7, obj.getOfferEndDate());
            preparedStatement.setString(8, obj.getOfferType());
            preparedStatement.setLong(9, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareUpdateStatement(PreparedStatement preparedStatement, Job obj){
        try{
            preparedStatement.setString(1, obj.getAddress());
            preparedStatement.setInt(2, obj.getWage());
            preparedStatement.setString(3, obj.getDescription());
            preparedStatement.setString(4, obj.getSchedule());
            preparedStatement.setDate(5, obj.getOfferBeginDate());
            preparedStatement.setDate(6, obj.getOfferEndDate());
            preparedStatement.setString(7, obj.getOfferType());
            preparedStatement.setLong(8, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement preparedStatement, Job obj){
        try{
            preparedStatement.setLong(1, obj.getIdentityKey());
            preparedStatement.setLong(2, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
