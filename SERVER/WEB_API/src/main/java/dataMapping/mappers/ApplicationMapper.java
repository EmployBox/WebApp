package dataMapping.mappers;


import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.Application;
import util.Streamable;

import java.sql.*;

public class ApplicationMapper extends AbstractMapper<Application, String> {
    public ApplicationMapper() {
        super(Application.class,
            PreparedStatement.class
            ApplicationMapper::prepareInsertStatement,
            ApplicationMapper::prepareUpdateStatement,
            ApplicationMapper::prepareDeleteStatement
        );
    }

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
