package isel.ps.EmployBox.dal.dataMapping.mappers;

import isel.ps.EmployBox.dal.dataMapping.utils.MapperRegistry;
import isel.ps.EmployBox.dal.dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import isel.ps.EmployBox.dal.domainModel.*;
import isel.ps.EmployBox.dal.util.Streamable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurriculumMapper extends AbstractMapper<Curriculum, String>{

    public CurriculumMapper() {
        super(
                Curriculum.class,
                PreparedStatement.class,
                CurriculumMapper::prepareInsertStatement,
                null,
                CurriculumMapper::prepareDeleteStatement
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
            Streamable<CurriculumExperience> curriculumExperiences = ((CurriculumExperienceMapper) MapperRegistry.getMapper(JobExperience.class)).findExperiences(accountId, curriculumId);

            Curriculum curriculum = Curriculum.load(accountId, curriculumId, title, version, previousJobs, academicBackground, project, curriculumExperiences);
            identityMap.put(curriculum.getIdentityKey(), curriculum);

            return curriculum;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareInsertStatement(PreparedStatement statement, Curriculum obj){
        try{
            statement.setLong(1, obj.getAccountId());
            statement.setLong(2, obj.getCurriculumId());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, Curriculum obj){
        try{
            statement.setLong(1, obj.getAccountId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setLong(3, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}