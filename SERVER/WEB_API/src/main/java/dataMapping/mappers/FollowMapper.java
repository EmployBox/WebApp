package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.Follow;
import util.Streamable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FollowMapper extends AbstractMapper<Follow, String> {
  public FollowMapper() {
        super(
                Follow.class,
                PreparedStatement.class,
                FollowMapper::prepareStatement,
                null,
                FollowMapper::prepareStatement
        );
    }

    public Streamable<Follow> findFollowingForAccount(long accountId){
        return findWhere(new Pair<>("AccountIdFrom", accountId));
    }

    @Override
    Follow mapper(ResultSet rs) throws DataMapperException {
        try{
            long accountIdFrom = rs.getLong(1);
            long accountIdDest = rs.getLong(2);

            Follow follow = Follow.load(accountIdFrom, accountIdDest);
            identityMap.put(follow.getIdentityKey(), follow);

            return follow;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareStatement(PreparedStatement statement, Follow obj){
        try{
            statement.setLong(1, obj.getAccountIdFrom());
            statement.setLong(2, obj.getAccountIdDest());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
