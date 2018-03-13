package isel.ps.EmployBox.dataMapping.mappers;


import isel.ps.EmployBox.dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import isel.ps.EmployBox.model.Application;
import isel.ps.EmployBox.util.Streamable;

import java.sql.*;

public class ApplicationMapper extends AbstractMapper<Application, String> {
    public ApplicationMapper() {
        super(Application.class,
            PreparedStatement.class,
            ApplicationMapper::prepareInsertStatement,
            ApplicationMapper::prepareUpdateStatement,
            ApplicationMapper::prepareDeleteStatement
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

    private static Application prepareInsertStatement(PreparedStatement statement, Application obj) {
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setLong(3, obj.getJobId());
            statement.setDate(4, obj.getDate());
            executeUpdate(statement);

            long version = getVersion(statement);

            return new Application(obj.getUserId(), obj.getJobId(), obj.getCurriculumId(), obj.getDate(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Application prepareUpdateStatement(PreparedStatement statement, Application obj) {
        try{
            statement.setDate(1, obj.getDate());
            executeUpdate(statement);

            long version = getVersion(statement);

            return new Application(obj.getUserId(), obj.getJobId(), obj.getCurriculumId(), obj.getDate(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Application prepareDeleteStatement(PreparedStatement statement, Application obj) {
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
            executeUpdate(statement);

            return null;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
