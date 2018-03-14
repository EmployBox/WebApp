package isel.ps.EmployBox.dal.dataMapping.mappers;

import isel.ps.EmployBox.dal.dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import isel.ps.EmployBox.dal.domainModel.CurriculumExperience;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CurriculumExperienceMapper extends AbstractMapper<CurriculumExperience,String> {
    public CurriculumExperienceMapper() {
        super(
                CurriculumExperience.class,
                PreparedStatement.class,
                CurriculumExperienceMapper::prepareWriteStatement,
                CurriculumExperienceMapper::prepareWriteStatement,
                CurriculumExperienceMapper::prepareDeleteStatement
        );
    }

    @Override
    CurriculumExperience mapper(ResultSet rs) throws DataMapperException {
        try {
            long userID = rs.getLong(0);
            long curriculumId = rs.getLong(1);
            String competences = rs.getString(2);
            short years = rs.getShort(3);
            long version = rs.getLong(4);

            CurriculumExperience curriculumExperience = CurriculumExperience.load(userID, curriculumId, competences, years, version);
            return curriculumExperience;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    public CompletableFuture<List<CurriculumExperience>> findExperiences(long accountId, long curriculumId){
        return findWhere(new Pair<>("userId", accountId),new Pair<>("curriculumId", curriculumId));
    }

    private static CurriculumExperience prepareWriteStatement(PreparedStatement statement, CurriculumExperience obj){
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setString(3,obj.getCompetences());
            statement.setShort(4, obj.getYears());
            executeUpdate(statement);

            long version = getVersion(statement);

            return new CurriculumExperience(obj.getUserId(), obj.getCurriculumId(), obj.getCompetences(), obj.getYears(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static CurriculumExperience prepareDeleteStatement(PreparedStatement statement, CurriculumExperience obj){
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
