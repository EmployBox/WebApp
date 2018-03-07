package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperSettings;
import javafx.util.Pair;
import model.DomainObject;
import model.Rating;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Streamable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingMapper extends AbstractMapper<Rating, String> {
    private static final String SELECT_QUERY = "SELECT AccountIdFrom, AccountIdTo, moderatorId, ratingValue, [status], [version] FROM Rating";
    private static final String INSERT_QUERY = "INSERT INTO Rating (AccountIdFrom, AccountIdTo, moderatorId, ratingValue, [status]) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE Rating SET moderatorId = ? AND ratingValue = ? AND [status] = ? WHERE AccountIdFrom = ? AND AccountIdTo = ? AND [version] = ?";
    private static final String DELETE_QUERY = "DELETE FROM Rating WHERE AccountIdFrom = ? AND AccountIdTo = ? AND [version] = ?";

    public RatingMapper() {
        super(
                new MapperSettings<>(INSERT_QUERY, PreparedStatement.class, RatingMapper::prepareInsertStatement),
                new MapperSettings<>(UPDATE_QUERY, PreparedStatement.class, RatingMapper::prepareUpdateStatement),
                new MapperSettings<>(DELETE_QUERY, PreparedStatement.class, RatingMapper::prepareDeleteStatement)
        );
    }

    public Streamable<Rating> findRatingsForAccount(long accountID) {
        return findWhere(new Pair<>("accountIdFrom", accountID));
    }

    public Streamable<Rating> findModeratedRatingsForModerator(long moderatorID) {
        return findWhere(new Pair<>("moderatorId", moderatorID));
    }

    @Override
    Rating mapper(ResultSet rs) throws DataMapperException {
        try{
            long accountIdFrom = rs.getLong(1);
            long accountIdTo = rs.getLong(2);
            long moderatorId = rs.getLong(3);
            double ratingValue = rs.getDouble(4);
            boolean status = rs.getBoolean(5);
            long version = rs.getLong(6);

            Rating rating = Rating.load(accountIdFrom, accountIdTo, moderatorId, ratingValue, status, version);
            identityMap.put(rating.getIdentityKey(), rating);

            return rating;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }

    private static void prepareInsertStatement(PreparedStatement statement, Rating obj){
        try{
            statement.setLong(1, obj.getAccountIdFrom());
            statement.setLong(2, obj.getAccountIdTo());
            statement.setLong(3, obj.getModeratorId());
            statement.setDouble(4, obj.getRatingValue());
            statement.setBoolean(5, obj.isApproved());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareUpdateStatement(PreparedStatement statement, Rating obj){
        try{
            statement.setLong(1, obj.getModeratorId());
            statement.setDouble(2, obj.getRatingValue());
            statement.setBoolean(3, obj.isApproved());
            statement.setLong(4, obj.getAccountIdFrom());
            statement.setLong(5, obj.getAccountIdTo());
            statement.setLong(6, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteStatement(PreparedStatement statement, Rating obj){
        try{
            statement.setLong(1, obj.getAccountIdFrom());
            statement.setLong(2, obj.getAccountIdTo());
            statement.setLong(3, obj.getVersion());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
