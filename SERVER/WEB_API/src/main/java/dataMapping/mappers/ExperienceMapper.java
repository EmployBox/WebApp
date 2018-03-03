package dataMapping.mappers;


import dataMapping.exceptions.DataMapperException;

import model.Experience;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class ExperienceMapper extends AbstractMapper<Experience, Long> {

    public Stream<Experience> findJobExperiences(long jobId){
        String query = "Select experienceId, competence, years from Experience where experienceId in (Select experienceId from Job_Experience where jobId = ?)";
        return executeQuery(
                query,
                null,
                statement -> {
                    SQLException sqlException = null;
                    try{
                        statement.setLong(1, jobId);
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                }
        );
    }

    @Override
    public Experience mapper(ResultSet rs) throws DataMapperException {
        try {
            long experienceId = rs.getLong("experienceId");
            String competence = rs.getString("Competence");
            short years = rs.getShort("years");
            long version = rs.getLong("[version]");

            Experience experience = Experience.load(experienceId, competence, years, version);
            identityMap.put(experienceId, experience);

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
