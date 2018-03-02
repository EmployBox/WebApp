package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import model.Curriculum;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurriculumMapper extends AbstractMapper<Curriculum, String>{
    private final String SELECT_QUERY = "SELECT AccountId, CurriculumId FROM Curriculum WHERE AccountId = ? AND CurriculumId = ?";
    private final String INSERT_QUERY = "INSERT INTO Curriculum (AccountId, CurriculumId) VALUES (?, ?)";
    private final String DELETE_QUERY = "DELETE FROM Curriculum WHERE AccountId = ? AND CurriculumId = ?";

    @Override
    protected String findByPKStatement() {
        return null;
    }

    @Override
    Curriculum mapper(ResultSet rs) throws DataMapperException {
        try{
            long accountId = rs.getLong("AccountId");
            long curriculumId = rs.getLong("CurriculumId");

            Curriculum curriculum = Curriculum.load(accountId, curriculumId, null, null, null);
            getIdentityMap().put(curriculum.getIdentityKey(), curriculum);

            return curriculum;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    public void insert(Curriculum obj) {
        //TODO Procedure
    }

    @Override
    public void update(Curriculum obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Curriculum obj) {
        //TODO Procedure
    }
}
