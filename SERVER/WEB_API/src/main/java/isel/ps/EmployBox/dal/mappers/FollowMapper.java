package isel.ps.EmployBox.dal.mappers;

import isel.ps.EmployBox.dal.exceptions.DataMapperException;
import isel.ps.EmployBox.dal.util.SQLUtils;
import javafx.util.Pair;
import isel.ps.EmployBox.dal.model.Follows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<List<Follows>> findFollowingForAccount(long accountId){
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

    private static Follows prepareStatement(PreparedStatement statement, Follows obj){
        try{
            statement.setLong(1, obj.getAccountIdFrom());
            statement.setLong(2, obj.getAccountIdDest());
            SQLUtils.executeUpdate(statement);

            long version = SQLUtils.getVersion(statement);

            return new Follows(obj.getAccountIdFrom(), obj.getAccountIdDest());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
