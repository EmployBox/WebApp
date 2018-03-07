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
    private static final String SELECT_QUERY = "SELECT userId, curriculumId, name, [description], [version] FROM Project";
    private static final String INSERT_QUERY = "INSERT INTO Project (userId, curriculumId, name, [description]) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE Project SET name = ?, [description] = ? WHERE userId = ? AND curriculumId = ? AND [version] = ?";
    private static final String DELETE_QUERY = "DELETE FROM Project WHERE userId = ? AND curriculumId = ? AND [version] = ?";

    public ProjectMapper() {
        super(
                new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, ProjectMapper::prepareInsertStatement),
                new MapperSettings<>(UPDATE_QUERY, PreparedStatement.class, ProjectMapper::prepareUpdateStatement),
                new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, ProjectMapper::prepareDeleteStatement)
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

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
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
