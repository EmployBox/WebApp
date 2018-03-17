package isel.ps.employbox.dal.mappers;

import isel.ps.employbox.dal.util.MapperRegistry;
import isel.ps.employbox.dal.exceptions.DataMapperException;
import isel.ps.employbox.dal.model.Chat;
import isel.ps.employbox.dal.model.Comment;
import isel.ps.employbox.dal.model.Moderator;
import isel.ps.employbox.dal.model.Rating;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
            Float rating = rs.getFloat("Rating");
            long version = rs.getLong("[version]");

            Supplier<List<Comment>> comments = ((CommentMapper) MapperRegistry.getMapper(Comment.class)).findCommentsForAccount(accountID)::join;
            Supplier<List<Chat>> chats = ((ChatMapper) MapperRegistry.getMapper(Chat.class)).findForAccount(accountID)::join;
            Supplier<List<Rating>> ratings = ((RatingMapper) MapperRegistry.getMapper(Rating.class)).findRatingsForAccount(accountID)::join;
            Supplier<List<Rating>> ratingsModerated = ((RatingMapper) MapperRegistry.getMapper(Rating.class)).findModeratedRatingsForModerator(accountID)::join;

            Moderator moderator = Moderator.load(accountID, email, passwordHash, rating, version, comments, chats, ratings, ratingsModerated );
            identityMap.put(accountID, moderator);

            return moderator;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    private static Moderator prepareWriteProcedure(CallableStatement cs,  Moderator obj) {
        try {
            cs.setString(1, obj.getEmail());
            cs.setDouble(2, obj.getRating());
            cs.setString(3, obj.getPassword());
            cs.registerOutParameter(4, Types.BIGINT);//ACCOUNTID
            cs.registerOutParameter(5, Types.BIGINT);//VERSION
            cs.execute();

            long moderatorId = cs.getLong(4);
            long version = cs.getLong(5);

            return new Moderator(
                    moderatorId,
                    obj.getEmail(),
                    obj.getPassword(),
                    obj.getRating(),
                    version,
                    ()->obj.getComments().collect(Collectors.toList()),
                    ()->obj.getChats().collect(Collectors.toList()),
                    ()->obj.getRatings().collect(Collectors.toList()),
                    ()->obj.getRatings().collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static Moderator prepareDeleteProcedure(CallableStatement callableStatement, Moderator obj){
        try {
            callableStatement.setLong(1, obj.getIdentityKey());
            callableStatement.execute();
            return null;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
