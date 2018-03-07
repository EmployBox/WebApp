package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.JobExperience;
import util.Streamable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobExperienceMapper extends AbstractMapper<JobExperience,Long> {
    private static final String SELECT_QUERY = "SELECT competences, years FROM Job_Experience where jobId = ?";
    private static final String INSERT_QUERY = "INSERT INTO Job_Experience (jobId,competences, years) VALUES (?, ?, ?) where jobId = ?";
    private static final String UPDATE_QUERY = "UPDATE Job_Experience SET competences = ?, years = ? VALUES (?, ?) where jobId = ?";
    private static final String DELETE_QUERY = "DELETE FROM Job_Experience where jobId = ?";

    public JobExperienceMapper() {
        super(
                new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, JobExperienceMapper::prepareWriteStatement),
                new MapperSettings<>(UPDATE_QUERY, PreparedStatement.class, JobExperienceMapper::prepareWriteStatement),
                new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, JobExperienceMapper::prepareDeleteStatement)
        );
    }

    @Override
    JobExperience mapper(ResultSet rs) throws DataMapperException {
        try {
            long jobId = rs.getLong(0);
            String competences = rs.getString(2);
            short years = rs.getShort(3);
            long version = rs.getLong(4);

            JobExperience jobExperience = JobExperience.load(jobId, competences, years, version);
            return jobExperience;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    public Streamable<JobExperience> findExperiences(long jobId){
        return findWhere(new Pair<>("jobId", jobId));
    }

    private static void prepareWriteStatement(PreparedStatement statement, JobExperience obj){
        try{
            statement.setLong(1, obj.getJobId());
            statement.setString(3,obj.getCompeteces());
            statement.setShort(4, obj.getYears());

        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, JobExperience obj){
        try{
            statement.setLong(1, obj.getJobId());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }
}

