package isel.ps.EmployBox.dal.mappers;

import isel.ps.EmployBox.dal.exceptions.DataMapperException;
import isel.ps.EmployBox.dal.util.SQLUtils;
import javafx.util.Pair;
import isel.ps.EmployBox.dal.model.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<List<Project>> findForUserAndCurriculum(long accountId, long curriculumId){
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

    private static Project prepareInsertStatement(PreparedStatement statement, Project obj){
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setString(3, obj.getName());
            statement.setString(4, obj.getDescription());
            SQLUtils.executeUpdate(statement);

            long version = SQLUtils.getVersion(statement);

            return new Project(obj.getUserId(), obj.getCurriculumId(), obj.getName(), obj.getDescription(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Project prepareUpdateStatement(PreparedStatement statement, Project obj){
        try{
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getDescription());
            statement.setLong(3, obj.getUserId());
            statement.setLong(4, obj.getCurriculumId());
            statement.setLong(5, obj.getVersion());
            SQLUtils.executeUpdate(statement);

            long version = SQLUtils.getVersion(statement);

            return new Project(obj.getUserId(), obj.getCurriculumId(), obj.getName(), obj.getDescription(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Project prepareDeleteStatement(PreparedStatement statement, Project obj){
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setLong(3, obj.getVersion());
            SQLUtils.executeUpdate(statement);
            return null;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
