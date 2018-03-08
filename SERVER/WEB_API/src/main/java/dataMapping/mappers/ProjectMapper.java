package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.Project;
import util.Streamable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectMapper extends AbstractMapper<Project, String> {
    public ProjectMapper() {
        super(
                Project.class,
                PreparedStatement.class,
                ProjectMapper::prepareInsertStatement,
                ProjectMapper::prepareUpdateStatement,
                ProjectMapper::prepareDeleteStatement
        );
    }

    public Streamable<Project> findForUserAndCurriculum(long accountId, long curriculumId){
        return findWhere(new Pair<>("userId", accountId), new Pair<>("curriculumId", curriculumId));
    }

    @Override
    Project mapper(ResultSet rs) throws DataMapperException {
        try{
            long accountId = rs.getLong(1);
            long curriculumId = rs.getLong(2);
            String name = rs.getString(3);
            String description = rs.getString(4);
            long version = rs.getLong(5);

            Project project = Project.load(accountId, curriculumId, name, description, version);
            identityMap.put(project.getIdentityKey(), project);

            return project;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareInsertStatement(PreparedStatement statement, Project obj){
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setString(3, obj.getName());
            statement.setString(4, obj.getDescription());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareUpdateStatement(PreparedStatement statement, Project obj){
        try{
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getDescription());
            statement.setLong(3, obj.getUserId());
            statement.setLong(4, obj.getCurriculumId());
            statement.setLong(5, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, Project obj){
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setLong(3, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
