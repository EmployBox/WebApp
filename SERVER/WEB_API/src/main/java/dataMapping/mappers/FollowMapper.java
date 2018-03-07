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
    private static final String SELECT_QUERY = "SELECT AccountIdFrom, AccountIdDest FROM Follows";
    private static final String INSERT_QUERY = "INSERT INTO Follows (AccountIdFrom, AccountIdDest) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM Follows WHERE AccountIdFrom = ? AND AccountIdDest = ?";

    public FollowMapper() {
        super(
                new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, FollowMapper::prepareStatement),
                null,
                new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, FollowMapper::prepareStatement)
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

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
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
