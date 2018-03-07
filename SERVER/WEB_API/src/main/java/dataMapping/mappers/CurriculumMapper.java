package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import model.Curriculum;
import util.Streamable;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class CurriculumMapper extends MapperByProcedure<Curriculum, String>{
    private final String SELECT_QUERY = "SELECT userId, CurriculumId FROM Curriculum";
    private final String INSERT_QUERY = "INSERT INTO Curriculum (userId, CurriculumId) VALUES (?, ?)";
    private final String DELETE_QUERY = "DELETE FROM Curriculum WHERE AccountId = ? AND CurriculumId = ?";

    public Streamable<Curriculum> findCurriculumsForAccount(long accountId){
        return findWhere(new Pair<>("userId", accountId));
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
    protected Consumer<CallableStatement> prepareUpdateProcedureArguments(Curriculum obj) {
        return null;
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