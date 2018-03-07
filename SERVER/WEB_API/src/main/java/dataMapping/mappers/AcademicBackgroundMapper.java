package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.AcademicBackground;
import util.Streamable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AcademicBackgroundMapper extends AbstractMapper<AcademicBackground, String> {
    private static final String SELECT_QUERY = "SELECT userId, curriculumId, beginDate, endDate, studyArea, institution, degreeObtained, [version] FROM AcademicBackground";
    private static final String INSERT_QUERY = "INSERT INTO AcademicBackground (userId, curriculumId, beginDate, endDate, studyArea, institution, degreeObtained) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE AcademicBackground SET beginDate = ?, endDate = ?, studyArea = ?, institution = ?, degreeObtained = ? WHERE userId = ? AND curriculumId = ? AND [version] = ?";
    private static final String DELETE_QUERY = "DELETE FROM AcademicBackground WHERE userId = ? AND curriculumId = ? AND [version] = ?";

    public AcademicBackgroundMapper() {
        super(
            new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, AcademicBackgroundMapper::prepareInsertStatement),
            new MapperSettings<>(UPDATE_QUERY, PreparedStatement.class, AcademicBackgroundMapper::prepareUpdateStatement),
            new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, AcademicBackgroundMapper::prepareDeleteStatement));
    }

    public Streamable<AcademicBackground> findForUserAndCurriculum(long accountId, long curriculumId){
        return findWhere(new Pair<>("userId", accountId), new Pair<>("curriculumId", curriculumId));
    }

    @Override
    AcademicBackground mapper(ResultSet rs) throws DataMapperException {
        try{
            long accountId = rs.getLong(1);
            long curriculumId = rs.getLong(2);
            Date beginDate = rs.getDate(3);
            Date endDate = rs.getDate(4);
            String studyArea = rs.getString(5);
            String institution = rs.getString(6);
            String degreeObtained = rs.getString(7);
            long version = rs.getLong(8);

            AcademicBackground academicBackground = AcademicBackground.load(accountId, curriculumId, beginDate, endDate, studyArea, institution, degreeObtained, version);
            identityMap.put(String.format("%d-%d", accountId, curriculumId), academicBackground);

            return academicBackground;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }


    private static void prepareInsertStatement(PreparedStatement statement, AcademicBackground obj){
        try{
            statement.setLong(1, obj.getAccountID());
            statement.setLong(2, obj.getCurriculumId());
            statement.setDate(3, obj.getBeginDate());
            statement.setDate(4, obj.getEndDate());
            statement.setString(5, obj.getStudyArea());
            statement.setString(6, obj.getInstitution());
            statement.setString(7, obj.getDegreeObtained());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareUpdateStatement(PreparedStatement statement, AcademicBackground obj){
        try{
            statement.setDate(1, obj.getBeginDate());
            statement.setDate(2, obj.getEndDate());
            statement.setString(3, obj.getStudyArea());
            statement.setString(4, obj.getInstitution());
            statement.setString(5, obj.getDegreeObtained());
            statement.setLong(6, obj.getAccountID());
            statement.setLong(7, obj.getCurriculumId());
            statement.setLong(8, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, AcademicBackground obj){
        try{
            statement.setLong(1, obj.getAccountID());
            statement.setLong(2, obj.getCurriculumId());
            statement.setLong(3, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
