package isel.ps.EmployBox.dal.mappers;

import isel.ps.EmployBox.dal.exceptions.DataMapperException;
import isel.ps.EmployBox.dal.util.SQLUtils;
import javafx.util.Pair;
import isel.ps.EmployBox.dal.model.AcademicBackground;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AcademicBackgroundMapper extends AbstractMapper<AcademicBackground, String> {
    public AcademicBackgroundMapper() {
        super(AcademicBackground.class,
                PreparedStatement.class,
                AcademicBackgroundMapper::prepareInsertStatement,
                AcademicBackgroundMapper::prepareUpdateStatement,
                AcademicBackgroundMapper::prepareDeleteStatement);
    }

    public CompletableFuture<List<AcademicBackground>> findForUserAndCurriculum(long accountId, long curriculumId){
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

    private static AcademicBackground prepareInsertStatement(PreparedStatement statement, AcademicBackground obj){
        try{
            statement.setLong(1, obj.getAccountID());
            statement.setLong(2, obj.getCurriculumId());
            statement.setDate(3, obj.getBeginDate());
            statement.setDate(4, obj.getEndDate());
            statement.setString(5, obj.getStudyArea());
            statement.setString(6, obj.getInstitution());
            statement.setString(7, obj.getDegreeObtained());
            SQLUtils.executeUpdate(statement);

            long version = SQLUtils.getVersion(statement);

            return new AcademicBackground(obj.getAccountID(), obj.getCurriculumId(), obj.getBeginDate(), obj.getEndDate(), obj.getStudyArea(), obj.getInstitution(), obj.getDegreeObtained(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static AcademicBackground prepareUpdateStatement(PreparedStatement statement, AcademicBackground obj){
        try{
            statement.setDate(1, obj.getBeginDate());
            statement.setDate(2, obj.getEndDate());
            statement.setString(3, obj.getStudyArea());
            statement.setString(4, obj.getInstitution());
            statement.setString(5, obj.getDegreeObtained());
            statement.setLong(6, obj.getAccountID());
            statement.setLong(7, obj.getCurriculumId());
            statement.setLong(8, obj.getVersion());
            SQLUtils.executeUpdate(statement);

            long version = SQLUtils.getVersion(statement);

            return new AcademicBackground(obj.getAccountID(), obj.getCurriculumId(), obj.getBeginDate(), obj.getEndDate(), obj.getStudyArea(), obj.getInstitution(), obj.getDegreeObtained(), version);
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static AcademicBackground prepareDeleteStatement(PreparedStatement statement, AcademicBackground obj){
        try{
            statement.setLong(1, obj.getAccountID());
            statement.setLong(2, obj.getCurriculumId());
            statement.setLong(3, obj.getVersion());
            SQLUtils.executeUpdate(statement);
            return null;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
