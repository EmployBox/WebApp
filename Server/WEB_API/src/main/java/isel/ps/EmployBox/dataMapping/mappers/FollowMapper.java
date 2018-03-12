package isel.ps.EmployBox.dataMapping.mappers;

import isel.ps.EmployBox.dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import isel.ps.EmployBox.model.Follows;
import isel.ps.EmployBox.util.Streamable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FollowMapper extends AbstractMapper<Follows, String> {
  public FollowMapper() {
        super(
                Follows.class,
                PreparedStatement.class,
                FollowMapper::prepareStatement,
                null,
                FollowMapper::prepareStatement
        );
    }

    public Streamable<Follows> findFollowingForAccount(long accountId){
        return findWhere(new Pair<>("AccountIdFrom", accountId));
    }

    @Override
    Follows mapper(ResultSet rs) throws DataMapperException {
        try{
            long accountIdFrom = rs.getLong(1);
            long accountIdDest = rs.getLong(2);

            Follows follows = Follows.load(accountIdFrom, accountIdDest);
            identityMap.put(follows.getIdentityKey(), follows);

            return follows;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareStatement(PreparedStatement statement, Follows obj){
        try{
            statement.setLong(1, obj.getAccountIdFrom());
            statement.setLong(2, obj.getAccountIdDest());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
