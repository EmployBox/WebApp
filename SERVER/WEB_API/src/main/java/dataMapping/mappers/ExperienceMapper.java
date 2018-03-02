package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.ConnectionManager;
import dataMapping.utils.MapperRegistry;
import model.Experience;
import model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class ExperienceMapper extends AbstractMapper<Experience, Long> {

    public Stream<Experience> findJobExperiences(Object jobId){
        Connection con = ConnectionManager.getConnectionManager().getConnection();
        PreparedStatement statement;
        try {
            statement = con.prepareStatement("Select experienceId, competence, years from Experience where experienceId in (Select experienceId from Job_Experience where jobId = ?)");

            statement.setLong(1, (Long) jobId);

            ExperienceMapper experienceMapper = (ExperienceMapper) MapperRegistry.getMapper(Experience.class);
            return experienceMapper.stream(statement.executeQuery(), experienceMapper::mapper);
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    protected String findByPKStatement() {
        return null;
    }

    @Override
    public Experience mapper(ResultSet rs) throws DataMapperException {
        try {
            long experienceId = rs.getLong("experienceId");
            String competence = rs.getString("Competence");
            short years = rs.getShort("years");

            Experience experience = Experience.load(experienceId, competence, years);
            getIdentityMap().put(experienceId, experience);

            return experience;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    public void insert(Experience obj) {

    }

    @Override
    public void update(Experience obj) {

    }

    @Override
    public void delete(Experience obj) {

    }
}
