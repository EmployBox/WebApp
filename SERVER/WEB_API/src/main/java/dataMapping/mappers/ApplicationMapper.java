package dataMapping.mappers;


import dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import model.Application;
import util.Streamable;

import java.sql.*;

public class ApplicationMapper extends AbstractMapper<Application, String> {
    private String SELECT_QUERY =  "SELECT UserId, CurriculumId, JobId, [date] from [Application]";
    private String INSERT_QUERY =  "INSERT INTO [Application](userId, jobId, curriculumId, [date]) values (? , ? , ? , ?)";
    private String UPDATE_QUERY =  "UPDATE [Application] SET [date] = ? where where userId = ? AND JobId = ?";
    private String DELETE_QUERY =  "DELETE [Application] where userId = ? AND jobId = ?";

    public Streamable<Application> findJobApplications(long jobId){
        return findWhere(new Pair<>("jobId", jobId));
    }

    @Override
    Application mapper(ResultSet rs) throws DataMapperException {
        try {
            long userId = rs.getLong("userId");
            long jobId = rs.getLong("jobId");
            long curriculumId = rs.getLong("curriculumId");
            Date date = rs.getDate("[date]");
            long version = rs.getLong("[version]");

            Application application = Application.load(userId,jobId,curriculumId,date, version);
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

    @Override
    public void insert(Application obj) {
        executeSQLUpdate(
                INSERT_QUERY,
                obj,
                preparedStatement -> {
                    try{
                        preparedStatement.setLong(1, obj.getUserId());
                        preparedStatement.setLong(2, obj.getCurriculumId());
                        preparedStatement.setLong(3, obj.getJobId());
                        preparedStatement.setDate(4, obj.getDate());
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }

    @Override
    public void update(Application obj) {
        executeSQLUpdate(
                UPDATE_QUERY,
                obj,
                preparedStatement -> {
                    try{
                        preparedStatement.setDate(4, obj.getDate());
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }

    @Override
    public void delete(Application obj) {
        executeSQLUpdate(
                DELETE_QUERY,
                obj,
                preparedStatement -> {
                    try{
                        preparedStatement.setLong(1, obj.getUserId());
                        preparedStatement.setLong(2, obj.getCurriculumId());
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }
}
