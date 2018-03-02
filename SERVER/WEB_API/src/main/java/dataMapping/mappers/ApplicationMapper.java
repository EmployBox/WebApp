package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.ConnectionManager;
import dataMapping.utils.MapperRegistry;
import javafx.util.Pair;
import model.Application;
import model.Curriculum;
import model.Job;
import model.User;

import java.sql.*;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ApplicationMapper extends AbstractMapper<Application, String> {
    private String SELECT_QUERY =  "select UserId, CurriculumId, JobId, [date] from [Application] where userId = ? AND JobId = ? ";
    private String INSERT_QUERY =  "INSERT INTO [Application](userId, jobId, curriculumId, [date]) values (? , ? , ? , ?)";
    private String UPDATE_QUERY =  "UPDATE [Application] SET [date] = ? where where userId = ? AND JobId = ?";
    private String DELETE_QUERY =  "DELETE [Application] where userId = ? AND jobId = ?";

    public Stream<Pair<User, Curriculum>> findJobApplications(Object jobId){
        Connection con = ConnectionManager.getConnectionManager().getConnection();
        PreparedStatement statement;
        try {
            statement = con.prepareStatement("SELECT [User].accountId, [User].name, [User].summary, [User].photourl, ApiDataBase.[Application].curriculumId, ApiDataBase.[Application].[Date]\n" +
                    "FROM [User]\n" +
                    "INNER JOIN ApiDataBase.[Application]\n" +
                    "ON ApiDataBase.[Application].UserId = [User].accountId\n" +
                    "AND ApiDataBase.[Application].JobId = ?");

            statement.setLong(1, (Long) jobId);
            ResultSet rs = statement.executeQuery();

            UserMapper userMapper = (UserMapper) MapperRegistry.getMapper(User.class);
            CurriculumMapper curriculumMapper = (CurriculumMapper) MapperRegistry.getMapper(Curriculum.class);
            return StreamSupport.stream(new Spliterators.AbstractSpliterator<Pair<User, Curriculum>>(
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
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

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
