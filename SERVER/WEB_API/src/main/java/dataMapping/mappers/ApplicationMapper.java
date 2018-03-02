package dataMapping.mappers;


import dataMapping.exceptions.DataMapperException;

import model.Application;

import java.sql.*;
import java.util.stream.Stream;

public class ApplicationMapper extends AbstractMapper<Application, String> {
    private String SELECT_QUERY =  "select UserId, CurriculumId, JobId, [date] from [Application] where userId = ? AND JobId = ? ";
    private String INSERT_QUERY =  "INSERT INTO [Application](userId, jobId, curriculumId, [date]) values (? , ? , ? , ?)";
    private String UPDATE_QUERY =  "UPDATE [Application] SET [date] = ? where where userId = ? AND JobId = ?";
    private String DELETE_QUERY =  "DELETE [Application] where userId = ? AND jobId = ?";

    public Stream<Application> findJobApplications(long jobId){
        String query = "SELECT UserId, CurriculumId, JobId, [date] from [Application] WHERE JobId = ?";
        return executeQuery(
                query,
                null,
                preparedStatement -> {
                    SQLException sqlException = null;
                    try{
                        preparedStatement.setLong(1, jobId);
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                }
        );
    }

    @Override
    Application mapper(ResultSet rs) throws DataMapperException {
        try {
            long userId = rs.getLong("userId");
            long jobId = rs.getLong("jobId");
            long curriculumId = rs.getLong("curriculumId");
            Date date = rs.getDate("[date]");

            Application application = Application.load(userId,jobId,curriculumId,date);
            getIdentityMap().put(String.format("ApplicationPK %s %s",userId,jobId), application);

            return application;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    public void insert(Application obj) {
        executeSQLUpdate(
                INSERT_QUERY,
                obj,
                preparedStatement -> {
                    SQLException sqlException = null;
                    try{
                        preparedStatement.setLong(1, obj.getUserId());
                        preparedStatement.setLong(2, obj.getCurriculumId());
                        preparedStatement.setLong(3, obj.getJobId());
                        preparedStatement.setDate(4, obj.getDate());
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                }
        );
    }

    @Override
    public void update(Application obj) {
        executeSQLUpdate(
                UPDATE_QUERY,
                obj,
                preparedStatement -> {
                    SQLException sqlException = null;
                    try{
                        preparedStatement.setDate(4, obj.getDate());
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                }
        );
    }

    @Override
    public void delete(Application obj) {
        executeSQLUpdate(
                DELETE_QUERY,
                obj,
                preparedStatement -> {
                    SQLException sqlException = null;
                    try{
                        preparedStatement.setLong(1, obj.getUserId());
                        preparedStatement.setLong(2, obj.getCurriculumId());
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                }
        );
    }
}
