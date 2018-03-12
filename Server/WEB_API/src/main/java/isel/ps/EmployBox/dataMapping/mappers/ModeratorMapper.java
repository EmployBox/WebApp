package isel.ps.EmployBox.dataMapping.mappers;

import isel.ps.EmployBox.dataMapping.exceptions.DataMapperException;
import isel.ps.EmployBox.dataMapping.utils.MapperRegistry;
import isel.ps.EmployBox.dataMapping.utils.MapperSettings;
import isel.ps.EmployBox.model.*;
import isel.ps.EmployBox.util.Streamable;

import java.sql.*;

public class ModeratorMapper extends AccountMapper<Moderator> {

    public ModeratorMapper() {
        super(
                Moderator.class,
                ModeratorMapper::prepareWriteProcedure,
                ModeratorMapper::prepareWriteProcedure,
                ModeratorMapper::prepareDeleteProcedure
        );
    }

    @Override
    Moderator mapper(ResultSet rs) throws DataMapperException {
        try {
            long accountID = rs.getLong("AccountID");
            String email = rs.getString("Email");
            String passwordHash = rs.getString("passwordHash");
            Double rating = rs.getDouble("Rating");
            long version = rs.getLong("[version]");

            Streamable<Comment> comments = ((CommentMapper) MapperRegistry.getMapper(Comment.class)).findCommentsForAccount(accountID);
            Streamable<Chat> chats = ((ChatMapper) MapperRegistry.getMapper(Chat.class)).findForAccount(accountID);
            Streamable<Rating> ratings = ((RatingMapper) MapperRegistry.getMapper(Rating.class)).findRatingsForAccount(accountID);
            Streamable<Rating> ratingsModerated = ((RatingMapper) MapperRegistry.getMapper(Rating.class)).findModeratedRatingsForModerator(accountID);

            Moderator moderator = Moderator.load(accountID, email, passwordHash, rating, version, comments, chats, ratings, ratingsModerated );
            identityMap.put(accountID, moderator);

            return moderator;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    private static void prepareWriteProcedure(CallableStatement cs,  Moderator obj) {
        try {
            cs.setString(1, obj.getEmail());
            cs.setDouble(2, obj.getRating());
            cs.setString(3, obj.getPassword());
            cs.registerOutParameter(4, Types.BIGINT);
            cs.registerOutParameter(5, Types.NVARCHAR);
            cs.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteProcedure(CallableStatement callableStatement, Moderator obj){
        try {
            callableStatement.setLong(1, obj.getIdentityKey());
            callableStatement.registerOutParameter(2, Types.NVARCHAR);
            callableStatement.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
