package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.CurriculumExperience;
import util.Streamable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurriculumExperienceMapper extends AbstractMapper<CurriculumExperience,String> {
    private static final String SELECT_QUERY = "SELECT competences, years FROM Curriculum_Experience where userId = ? AND curriculumId = ?";
    private static final String INSERT_QUERY = "INSERT INTO Curriculum_Experience (competences, years) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE Curriculum_Experience SET competences = ?, years = ? VALUES (?, ?) where userId = ? AND curriculumId = ?";
    private static final String DELETE_QUERY = "DELETE FROM Curriculum_Experience WHERE userId = ? AND curriculumId = ?";

    public CurriculumExperienceMapper() {
        super(
                new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, CurriculumExperienceMapper::prepareWriteStatement),
                new MapperSettings<>(UPDATE_QUERY, PreparedStatement.class, CurriculumExperienceMapper::prepareWriteStatement),
                new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, CurriculumExperienceMapper::prepareDeleteStatement)
        );
    }

    @Override
    CurriculumExperience mapper(ResultSet rs) throws DataMapperException {
        try {
            long userID = rs.getLong(0);
            long curriculumId = rs.getLong(1);
            String competences = rs.getString(2);
            short years = rs.getShort(3);

            CurriculumExperience curriculumExperience = CurriculumExperience.load(userID, curriculumId, competences, years);
            return curriculumExperience;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    public Streamable<CurriculumExperience> findExperiences(long accountId, long curriculumId){
        return findWhere(new Pair<>("userId", accountId),new Pair<>("curriculumId", curriculumId));
    }

    private static void prepareWriteStatement(PreparedStatement statement, CurriculumExperience obj){
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
            statement.setString(3,obj.getCompetences());
            statement.setShort(4, obj.getYears());

        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, CurriculumExperience obj){
        try{
            statement.setLong(1, obj.getUserId());
            statement.setLong(2, obj.getCurriculumId());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }
}
