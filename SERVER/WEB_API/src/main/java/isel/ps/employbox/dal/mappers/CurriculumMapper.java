package isel.ps.employbox.dal.mappers;

import isel.ps.employbox.dal.util.MapperRegistry;
import isel.ps.employbox.dal.exceptions.DataMapperException;
import isel.ps.employbox.dal.util.SQLUtils;
import javafx.util.Pair;
import isel.ps.employbox.dal.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    public CompletableFuture<List<Curriculum>> findCurriculumsForAccount(long accountId){
        return findWhere(new Pair<>("userId", accountId));
    }

    @Override
    Curriculum mapper(ResultSet rs) throws DataMapperException {
        try{
            long accountId = rs.getLong("userId");
            long curriculumId = rs.getLong("curriculumId");
            String title = rs.getString("title");
            long version = rs.getLong("[version]");

            Supplier<List<PreviousJobs>> previousJobs = ((PreviousJobsMapper) MapperRegistry.getMapper(PreviousJobs.class)).findForUserAndCurriculum(accountId, curriculumId)::join;
            Supplier<List<AcademicBackground>> academicBackground = ((AcademicBackgroundMapper) MapperRegistry.getMapper(AcademicBackground.class)).findForUserAndCurriculum(accountId, curriculumId)::join;
            Supplier<List<Project>> project = ((ProjectMapper) MapperRegistry.getMapper(Project.class)).findForUserAndCurriculum(accountId, curriculumId)::join;
            Supplier<List<CurriculumExperience>> curriculumExperiences = ((CurriculumExperienceMapper) MapperRegistry.getMapper(JobExperience.class)).findExperiences(accountId, curriculumId)::join;

            Curriculum curriculum = Curriculum.load(accountId, curriculumId, title, version, previousJobs, academicBackground, project, curriculumExperiences);
            identityMap.put(curriculum.getIdentityKey(), curriculum);

            return curriculum;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Curriculum prepareInsertStatement(PreparedStatement statement, Curriculum obj){
        try{
            statement.setLong(1, obj.getAccountId());
            statement.setLong(2, obj.getCurriculumId());
            SQLUtils.executeUpdate(statement);

            long version = SQLUtils.getVersion(statement);
            long curriculumId = SQLUtils.getGeneratedKey(statement);

            return new Curriculum(
                    obj.getAccountId(),
                    curriculumId,
                    obj.getTitle(),
                    version,
                    ()->obj.getPreviousJobs().collect(Collectors.toList()),
                    ()->obj.getAcademicBackground().collect(Collectors.toList()),
                    ()->obj.getProjects().collect(Collectors.toList()),
                    ()->obj.getExperiences().collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Curriculum prepareDeleteStatement(PreparedStatement statement, Curriculum obj){
        try{
            statement.setLong(1, obj.getAccountId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setLong(3, obj.getVersion());
            SQLUtils.executeUpdate(statement);
            return null;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}