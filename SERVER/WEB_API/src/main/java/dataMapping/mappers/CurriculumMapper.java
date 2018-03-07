package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperRegistry;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.AcademicBackground;
import model.Curriculum;
import model.PreviousJobs;
import model.Project;
import util.Streamable;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class CurriculumMapper extends AbstractMapper<Curriculum, String>{
    private static final String SELECT_QUERY = "SELECT userId, curriculumId, title, [version] FROM Curriculum";
    private static final String INSERT_QUERY = "INSERT INTO Curriculum (userId, CurriculumId) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM Curriculum WHERE AccountId = ? AND CurriculumId = ? AND [version] = ?";

    //TODO Procedures
    public CurriculumMapper() {
        super(
                null,
                null,
                null
        );
    }

    public Streamable<Curriculum> findCurriculumsForAccount(long accountId){
        return findWhere(new Pair<>("userId", accountId));
    }

    @Override
    Curriculum mapper(ResultSet rs) throws DataMapperException {
        try{
            long accountId = rs.getLong("userId");
            long curriculumId = rs.getLong("curriculumId");
            String title = rs.getString("title");
            long version = rs.getLong("[version]");

            Streamable<PreviousJobs> previousJobs = ((PreviousJobsMapper) MapperRegistry.getMapper(PreviousJobs.class)).findForUserAndCurriculum(accountId, curriculumId);
            Streamable<AcademicBackground> academicBackground = ((AcademicBackgroundMapper) MapperRegistry.getMapper(AcademicBackground.class)).findForUserAndCurriculum(accountId, curriculumId);
            Streamable<Project> project = ((ProjectMapper) MapperRegistry.getMapper(Project.class)).findForUserAndCurriculum(accountId, curriculumId);

            Curriculum curriculum = Curriculum.load(accountId, curriculumId, title, version, previousJobs, academicBackground, project);
            identityMap.put(curriculum.getIdentityKey(), curriculum);

            return curriculum;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }
}