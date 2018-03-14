package isel.ps.EmployBox.dal.dataMapping.mappers;

import isel.ps.EmployBox.dal.dataMapping.utils.MapperRegistry;
import isel.ps.EmployBox.dal.dataMapping.exceptions.DataMapperException;
import isel.ps.EmployBox.dal.dataMapping.utils.SQLUtils;
import javafx.util.Pair;
import isel.ps.EmployBox.dal.domainModel.Application;
import isel.ps.EmployBox.dal.domainModel.Job;
import isel.ps.EmployBox.dal.domainModel.JobExperience;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JobMapper extends AbstractMapper<Job, Long> {
    public JobMapper() {
        super(
                Job.class,
                PreparedStatement.class,
                JobMapper::prepareInsertStatement,
                JobMapper::prepareUpdateStatement,
                JobMapper::prepareDeleteStatement
        );
    }

    public CompletableFuture<List<Job>> findForAccount(long accountID){
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

            Supplier<List<Application>> applications = ((ApplicationMapper) MapperRegistry.getMapper(Application.class)).findJobApplications(jobID)::join;
            Supplier<List<JobExperience>> jobExperiences = ((JobExperienceMapper) MapperRegistry.getMapper(JobExperience.class)).findExperiences(jobID)::join;

            Job job = Job.load(jobID, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version, applications, jobExperiences);
            identityMap.put(jobID, job);

            return job;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    private static Job prepareInsertStatement(PreparedStatement preparedStatement, Job obj){
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
            SQLUtils.executeUpdate(preparedStatement);

            long version = SQLUtils.getVersion(preparedStatement);
            long jobId = SQLUtils.getGeneratedKey(preparedStatement);

            return new Job(jobId, obj.getAccountID(), obj.getAddress(), obj.getWage(), obj.getDescription(), obj.getSchedule(), obj.getOfferBeginDate(), obj.getOfferEndDate(), obj.getOfferType(), version,
                    ()->obj.getApplications().collect(Collectors.toList()), ()->obj.getExperiences().collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Job prepareUpdateStatement(PreparedStatement preparedStatement, Job obj){
        try{
            preparedStatement.setString(1, obj.getAddress());
            preparedStatement.setInt(2, obj.getWage());
            preparedStatement.setString(3, obj.getDescription());
            preparedStatement.setString(4, obj.getSchedule());
            preparedStatement.setDate(5, obj.getOfferBeginDate());
            preparedStatement.setDate(6, obj.getOfferEndDate());
            preparedStatement.setString(7, obj.getOfferType());
            preparedStatement.setLong(8, obj.getVersion());
            SQLUtils.executeUpdate(preparedStatement);

            long version = SQLUtils.getVersion(preparedStatement);

            return new Job(obj.getIdentityKey(), obj.getAccountID(), obj.getAddress(), obj.getWage(), obj.getDescription(), obj.getSchedule(), obj.getOfferBeginDate(), obj.getOfferEndDate(),
                    obj.getOfferType(), version,()-> obj.getApplications().collect(Collectors.toList()),()-> obj.getExperiences().collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Job prepareDeleteStatement(PreparedStatement preparedStatement, Job obj){
        try{
            preparedStatement.setLong(1, obj.getIdentityKey());
            preparedStatement.setLong(2, obj.getVersion());
            SQLUtils.executeUpdate(preparedStatement);
            return null;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
