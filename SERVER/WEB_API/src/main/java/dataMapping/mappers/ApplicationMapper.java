package dataMapping.mappers;


import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.Application;
import util.Streamable;

import java.sql.*;

public class ApplicationMapper extends AbstractMapper<Application, String> {
    private static final String SELECT_QUERY =  "SELECT UserId, CurriculumId, JobId, [date] from [Application]";
    private static final String INSERT_QUERY =  "INSERT INTO [Application](userId, jobId, curriculumId, [date]) values (? , ? , ? , ?)";
    private static final String UPDATE_QUERY =  "UPDATE [Application] SET [date] = ? where where userId = ? AND JobId = ?";
    private static final String DELETE_QUERY =  "DELETE [Application] where userId = ? AND jobId = ?";

    public ApplicationMapper() {
        super(
            new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, ApplicationMapper::prepareInsertStatement),
            new MapperSettings<>(UPDATE_QUERY, PreparedStatement.class, ApplicationMapper::prepareUpdateStatement),
            new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, ApplicationMapper::prepareDeleteStatement)
        );
    }

    public Streamable<Application> findJobApplications(long jobId){
        return findWhere(new Pair<>("jobId", jobId));
    }

    public Streamable<Application> findUserApplications(long userId){
        return findWhere(new Pair<>("userId", userId));
    }

    @Override
    Application mapper(ResultSet rs) throws DataMapperException {
        try {
            long userId = rs.getLong("userId");
            long jobId = rs.getLong("jobId");
            long curriculumId = rs.getLong("curriculumId");
            Date date = rs.getDate("[date]");
            long version = rs.getLong("[version]");

            Application application = Application.load(userId, jobId, curriculumId, date, version);
            identityMap.put(String.format("ApplicationPK %s %s",userId,jobId), application);

            return application;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }


    private static void prepareInsertStatement(PreparedStatement statement, Application obj) {
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setLong(3, obj.getJobId());
            statement.setDate(4, obj.getDate());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareUpdateStatement(PreparedStatement statement, Application obj) {
        try{
            statement.setDate(4, obj.getDate());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, Application obj) {
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
