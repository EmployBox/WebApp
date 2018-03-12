package isel.ps.EmployBox.dataMapping.mappers;

import isel.ps.EmployBox.dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import isel.ps.EmployBox.model.*;
import isel.ps.EmployBox.util.Streamable;

import java.sql.*;
import java.util.stream.Stream;

import static isel.ps.EmployBox.dataMapping.utils.MapperRegistry.getMapper;

public class UserMapper extends AccountMapper<User> {
    public UserMapper() {
        super(
                User.class,
                UserMapper::prepareWriteProcedure,
                UserMapper::prepareWriteProcedure,
                UserMapper::prepareDeleteProcedure
        );
    }

    public Streamable<User> findFollowingUsers(long accountID) {
        Stream<Follows> following = ((FollowMapper) getMapper(Follows.class)).findFollowingForAccount(accountID).get();
        return () -> following.map(follows -> find(follows.getAccountIdFrom()));
    }

    public User find(long accountId){
        User[] user = new User[1];
        findWhere(new Pair<>("accountId", accountId)).get()
                .findFirst().ifPresent(u -> user[0] = u);
        return user[0];
    }

    public User findForEmail(String email){
        User[] user = new User[1];
        findWhere(new Pair<>("email", email)).get()
                .findFirst().ifPresent(u -> user[0] = u);
        return user[0];
    }

    @Override
    public User mapper(ResultSet rs) throws DataMapperException {
        try {
            long accountID = rs.getLong("AccountID");
            String email = rs.getString("Email");
            String passwordHash = rs.getString("password");
            Double rating = rs.getDouble("Rating");
            String name = rs.getString ("Name");
            String summary = rs.getString ("Summary");
            String photoUrl = rs.getString ("PhotoUrl");
            long version = rs.getLong("version");

            Streamable<Job> offeredJobs = ((JobMapper) getMapper(Job.class)).findForAccount(accountID);
            Streamable<Curriculum> curriculums = ((CurriculumMapper) getMapper(Curriculum.class)).findCurriculumsForAccount(accountID);
            Streamable<Application> applications = ((ApplicationMapper) getMapper(Application.class)).findUserApplications(accountID);
            Streamable<Chat> chats = ((ChatMapper) getMapper(Chat.class)).findForAccount(accountID);
            Streamable<Rating> ratings = ((RatingMapper) getMapper(Rating.class)).findRatingsForAccount(accountID);
            Streamable<Comment> comments = ((CommentMapper) getMapper(Comment.class)).findCommentsForAccount(accountID);
            Streamable<User> following = findFollowingUsers(accountID);

            User user = User.load(accountID, email, passwordHash, rating, version, name ,summary, photoUrl, offeredJobs, curriculums, applications, chats, comments , ratings, following);
            identityMap.put(accountID, user);

            return user;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    private static void prepareWriteProcedure(CallableStatement cs, User obj) {
        try {
            cs.setString(1, obj.getEmail());
            cs.setDouble(2, obj.getRating());
            cs.setString(3, obj.getPassword());
            cs.setString(4, obj.getName());
            cs.setString(5, obj.getSummary());
            cs.setString(6, obj.getPhotoUrl());
            cs.registerOutParameter(7, Types.BIGINT);
            cs.registerOutParameter(8, Types.BIGINT);
            cs.execute();

            long accountId = cs.getLong(7);
            long version = cs.getLong(8);

            obj = User.load(accountId, obj.getEmail(), obj.getPassword(), obj.getRating(), version, obj.getName(), obj.getSummary(), obj.getPhotoUrl(),
                    obj.getOfferedJobs(), obj.getCurriculums(), obj.getApplications(), obj.getChats(), obj.getComments(), obj.getRatings(), obj.getFollowing());
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteProcedure(CallableStatement callableStatement, User obj){
        try {
            callableStatement.setLong(1, obj.getIdentityKey());
            callableStatement.registerOutParameter(2, Types.NVARCHAR);
            callableStatement.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
