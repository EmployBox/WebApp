package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.ConnectionManager;
import dataMapping.utils.MapperRegistry;
import javafx.util.Pair;
import model.Curriculum;
import model.Experience;
import model.Job;
import model.User;

import java.sql.*;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JobMapper extends AbstractMapper<Job> {
    private final String SELECT_QUERY = "SELECT JobID, AccountID, Address, Wage, Description, Schedule, OfferBeginDate, OfferEndDate, OfferType, Version FROM Job WHERE JobID = ?";
    private final String INSERT_QUERY = "INSERT INTO Job (AccountID, Address, Wage, Description, Schedule, OfferBeginDate, OfferEndDate, OfferType, Version) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE Job SET Address = ?, Wage = ?, Description = ?, Schedule = ?, OfferBeginDate = ?, OfferEndDate = ?, OfferType = ?, Version = ? WHERE JobID = ? AND Version = ?";
    private final String DELETE_QUERY = "DELETE FROM Job WHERE JobID = ? AND Version = ?";

    @Override
    protected String findByPKStatement() {
        return SELECT_QUERY;
    }

    public Stream<Experience> findExperiences(Job job){
        Connection con = ConnectionManager.getConnectionManager().getConnection();
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

    public Stream<Pair<User, Curriculum>> findApplications(Job job){
        Connection con = ConnectionManager.getConnectionManager().getConnection();
        PreparedStatement statement;
        try {
            statement = con.prepareStatement("SELECT [User].accountId, [User].name, [User].summary, [User].photourl, Application.curriculumId, Application.Date \n" +
                    "FROM Application\n" +
                    "INNER JOIN [User] \n" +
                    "ON Application.accountId = [User].accountId\n" +
                    "AND Application.JobId = ?");

            statement.setLong(1, (Long) job.getIdentityKey());
            ResultSet rs = statement.executeQuery();

            UserMapper userMapper = (UserMapper) MapperRegistry.getMapper(User.class);
            CurriculumMapper curriculumMapper = (CurriculumMapper) MapperRegistry.getMapper(Curriculum.class);
            Stream<Pair<User, Curriculum>> applicants = StreamSupport.stream(new Spliterators.AbstractSpliterator<Pair<User, Curriculum>>(
                    Long.MAX_VALUE, Spliterator.ORDERED) {
                @Override
                public boolean tryAdvance(Consumer<? super Pair<User, Curriculum>> action) {
                    try {
                        if(!rs.next())return false;
                        action.accept(new Pair<>(userMapper.mapper(rs), curriculumMapper.mapper(rs)));
                        return true;
                    } catch (SQLException e) {
                        throw new DataMapperException(e.getMessage(), e);
                    }
                }
            }, false);

            job.setApplications(applicants);
            return applicants;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
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

            Job job = Job.load(jobID, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version);
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
                        preparedStatement.setString(2, obj.getAddress());
                        preparedStatement.setInt(3, obj.getWage());
                        preparedStatement.setString(4, obj.getDescription());
                        preparedStatement.setString(5, obj.getSchedule());
                        preparedStatement.setDate(6, obj.getOfferBeginDate());
                        preparedStatement.setDate(7, obj.getOfferEndDate());
                        preparedStatement.setString(8, obj.getOfferType());
                        preparedStatement.setLong(9, obj.getVersion());
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
                        preparedStatement.setString(1, obj.getAddress());
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
