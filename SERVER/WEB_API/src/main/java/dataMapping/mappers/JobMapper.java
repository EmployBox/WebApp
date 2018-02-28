package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperRegistry;
import model.Experience;
import model.Job;

import java.sql.*;
import java.util.stream.Stream;

public class JobMapper extends AbstractMapper<Job> {
    private final String SELECT_QUERY = "SELECT JobID, AccountID, Wage, Description, Schedule, OfferBeginDate, OfferEndDate, OfferType, Version FROM Job WHERE JobID = ?";
    private final String INSERT_QUERY = "INSERT INTO Job (AccountID, Wage, Description, Schedule, OfferBeginDate, OfferEndDate, OfferType, Version) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE Job SET Wage = ?, Description = ?, Schedule = ?, OfferBeginDate = ?, OfferEndDate = ?, OfferType = ?, Version = ? WHERE JobID = ? AND Version = ?";
    private final String DELETE_QUERY = "DELETE FROM Job WHERE JobID = ? AND Version = ?";

    @Override
    protected String findByPKStatement() {
        return SELECT_QUERY;
    }

    public Stream<Experience> findExperiences(Job job){
        Connection con = null;
        PreparedStatement statement;
        try {
            statement = con.prepareStatement("Select experienceId, competence, years from Experience where experienceId in (Select experienceId from Job_Experience where jobId = ?)");

            statement.setLong(1, (Long) job.getIdentityKey());

            ExperienceMapper experienceMapper = (ExperienceMapper) MapperRegistry.getMapper(Experience.class);
            Stream<Experience> experiences = experienceMapper.stream(statement.executeQuery(), experienceMapper::mapper);
            job.setExperiences(experiences);
            return experiences;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    public Job mapper(ResultSet rs) throws DataMapperException {
        try {
            long jobID = rs.getLong("JobID");
            long accountID = rs.getLong("AccountID");
            int wage = rs.getInt("Wage");
            String description = rs.getString("Description");
            String schedule = rs.getString("Schedule");
            Date offerBeginDate = rs.getDate("OfferBeginDate");
            Date offerEndDate = rs.getDate("OfferEndDate");
            String offerType = rs.getString("OfferType");
            long version = rs.getLong("Version");

            Job job = Job.load(jobID, accountID, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version);
            getIdentityMap().put(jobID, job);

            return job;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    public void insert(Job obj) {
        DBHelper(
                INSERT_QUERY,
                preparedStatement -> {
                    SQLException sqlException = null;
                    try{
                        preparedStatement.setLong(1, obj.getAccountID());
                        preparedStatement.setInt(2, obj.getWage());
                        preparedStatement.setString(3, obj.getDescription());
                        preparedStatement.setString(4, obj.getSchedule());
                        preparedStatement.setDate(5, obj.getOfferBeginDate());
                        preparedStatement.setDate(6, obj.getOfferEndDate());
                        preparedStatement.setString(7, obj.getOfferType());
                        preparedStatement.setLong(8, obj.getVersion());
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                },
                () -> getIdentityMap().put(obj.getIdentityKey(), obj)
        );
    }

    @Override
    public void update(Job obj) {
        DBHelper(
                UPDATE_QUERY,
                preparedStatement -> {
                    SQLException sqlException = null;
                    try{
                        preparedStatement.setInt(1, obj.getWage());
                        preparedStatement.setString(2, obj.getDescription());
                        preparedStatement.setString(3, obj.getSchedule());
                        preparedStatement.setDate(4, obj.getOfferBeginDate());
                        preparedStatement.setDate(5, obj.getOfferEndDate());
                        preparedStatement.setString(6, obj.getOfferType());
                        preparedStatement.setLong(7, obj.getVersion());
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                },
                () -> getIdentityMap().put(obj.getIdentityKey(), obj)
        );
    }

    @Override
    public void delete(Job obj) {
        DBHelper(
                DELETE_QUERY,
                preparedStatement -> {
                    SQLException sqlException = null;
                    try{
                        preparedStatement.setLong(1, (Long) obj.getIdentityKey());
                        preparedStatement.setLong(2, obj.getVersion());
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                },
                () -> getIdentityMap().remove(obj.getIdentityKey())
        );
    }
}
