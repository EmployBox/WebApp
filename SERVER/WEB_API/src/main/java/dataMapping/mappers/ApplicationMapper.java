package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import model.Application;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationMapper extends AbstractMapper<Application> {
    private String SELECT_QUERY =  "select UserId, CurriculumId, JobId, [date] from [Application] where userId = ? AND JobId = ? ";
    private String INSERT_QUERY =  "INSERT INTO [Application](userId, jobId, curriculumId, [date]) values (? , ? , ? , ?)";
    private String UPDATE_QUERY =  "UPDATE [Application] SET [date] = ? where where userId = ? AND JobId = ?";
    private String DELETE_QUERY =  "DELETE [Application] where userId = ? AND jobId = ?";

    @Override
    protected String findByPKStatement() {
        return SELECT_QUERY;
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
        DBHelper(
                INSERT_QUERY,
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
                },
                () -> getIdentityMap().put(obj.getIdentityKey(), obj)
        );
    }

    @Override
    public void update(Application obj) {
        DBHelper(
                UPDATE_QUERY,
                preparedStatement -> {
                    SQLException sqlException = null;
                    try{
                        preparedStatement.setDate(4, obj.getDate());
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                },
                () -> getIdentityMap().put(obj.getIdentityKey(), obj)
        );
    }

    @Override
    public void delete(Application obj) {
        DBHelper(
                DELETE_QUERY,
                preparedStatement -> {
                    SQLException sqlException = null;
                    try{
                        preparedStatement.setLong(1, obj.getUserId());
                        preparedStatement.setLong(2, obj.getCurriculumId());
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                },
                () -> getIdentityMap().remove(obj.getIdentityKey(), obj)
        );
    }
}
