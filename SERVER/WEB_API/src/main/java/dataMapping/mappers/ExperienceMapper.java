package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import model.Experience;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExperienceMapper extends AbstractMapper<Experience> {
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
