package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import model.Curriculum;
import util.Streamable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurriculumMapper extends AbstractMapper<Curriculum, String>{
    private final String SELECT_QUERY = "SELECT userId, CurriculumId FROM Curriculum";
    private final String INSERT_QUERY = "INSERT INTO Curriculum (userId, CurriculumId) VALUES (?, ?)";
    private final String DELETE_QUERY = "DELETE FROM Curriculum WHERE AccountId = ? AND CurriculumId = ?";

    private Streamable<Curriculum> findCurriculumsForAccount(long accountId){
        String query = "SELECT curriculumId FROM Curriculum WHERE userId = ?";
        return () -> executeQuery(
                query,
                null,
                statement -> {
                    try {
                        statement.setLong(1, accountId);
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }

    @Override
    Curriculum mapper(ResultSet rs) throws DataMapperException {
        try{
            long accountId = rs.getLong("userId");
            long curriculumId = rs.getLong("curriculumId");
            long version = rs.getLong("[version]");

            Curriculum curriculum = Curriculum.load(accountId, curriculumId, version, null, null, null);
            identityMap.put(curriculum.getIdentityKey(), curriculum);

            return curriculum;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
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