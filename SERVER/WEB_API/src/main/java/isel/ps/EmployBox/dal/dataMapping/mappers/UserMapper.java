package isel.ps.EmployBox.dal.dataMapping.mappers;

import isel.ps.EmployBox.dal.dataMapping.utils.MapperRegistry;
import isel.ps.EmployBox.dal.dataMapping.exceptions.DataMapperException;
import javafx.util.Pair;
import isel.ps.EmployBox.dal.domainModel.*;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UserMapper extends AccountMapper<User> {
    public UserMapper() {
        super(
                User.class,
                UserMapper::prepareInsertProcedure,
                UserMapper::prepareUpdateProcedure,
                UserMapper::prepareDeleteProcedure
        );
    }

    public CompletableFuture<List<User>> findFollowingUsers(long accountID) {
        CompletableFuture<List<Follows>> following = ((FollowMapper) MapperRegistry.getMapper(Follows.class)).findFollowingForAccount(accountID);
        return following.thenApply(l -> l.stream().map(follows -> find(follows.getAccountIdFrom())).collect(Collectors.toList()));

    }

    public User find(long accountId){
        return identityMap.getOrDefault(accountId,
                findWhere(new Pair<>("accountId", accountId)).thenApply(l->l.stream().findFirst().get()).join());
    }

    public User findForEmail(String email){
        return identityMap.values()
                .stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .orElseGet(findWhere(new Pair<>("email", email)).thenApply(l-> l.stream().findFirst().get())::join);
    }

    @Override
    public User mapper(ResultSet rs) throws DataMapperException {
        try {
            long accountID = rs.getLong("AccountID");
            String email = rs.getString("Email");
            String passwordHash = rs.getString("password");
            Float rating = rs.getFloat("Rating");
            String name = rs.getString ("Name");
            String summary = rs.getString ("Summary");
            String photoUrl = rs.getString ("PhotoUrl");
            long version = rs.getLong("version");

            Supplier<List<Job>> offeredJobs = ((JobMapper) MapperRegistry.getMapper(Job.class)).findForAccount(accountID)::join;
            Supplier<List<Curriculum>> curriculums = ((CurriculumMapper) MapperRegistry.getMapper(Curriculum.class)).findCurriculumsForAccount(accountID)::join;
            Supplier<List<Application>> applications = ((ApplicationMapper) MapperRegistry.getMapper(Application.class)).findUserApplications(accountID)::join;
            Supplier<List<Chat>> chats = ((ChatMapper) MapperRegistry.getMapper(Chat.class)).findForAccount(accountID)::join;
            Supplier<List<Rating>> ratings = ((RatingMapper) MapperRegistry.getMapper(Rating.class)).findRatingsForAccount(accountID)::join;
            Supplier<List<Comment>> comments = ((CommentMapper) MapperRegistry.getMapper(Comment.class)).findCommentsForAccount(accountID)::join;
            Supplier<List<User>> following = findFollowingUsers(accountID)::join;

            User user = User.load(accountID, email, passwordHash, rating, version, name ,summary, photoUrl, offeredJobs, curriculums, applications, chats, comments , ratings, following);
            identityMap.put(accountID, user);

            return user;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    private static User prepareInsertProcedure(CallableStatement cs, User obj) {
        try {
            cs.setString(1, obj.getEmail());
            cs.setDouble(2, obj.getRating());
            cs.setString(3, obj.getPassword());
            cs.setString(4, obj.getName());
            cs.setString(5, obj.getSummary());
            cs.setString(6, obj.getPhotoUrl());
            cs.registerOutParameter(7, Types.BIGINT);//ACCOUNTID
            cs.registerOutParameter(8, Types.BIGINT);//VERSION
            cs.execute();

            long accountId = cs.getLong(7);
            long version = cs.getLong(8);

            return new User(
                    accountId,
                    obj.getEmail(),
                    obj.getPassword(),
                    obj.getRating(),
                    version,
                    obj.getName(),
                    obj.getSummary(),
                    obj.getPhotoUrl(),
                    ()->obj.getOfferedJobs().collect(Collectors.toList()),
                    ()->obj.getCurriculums().collect(Collectors.toList()),
                    ()->obj.getApplications().collect(Collectors.toList()),
                    ()->obj.getChats().collect(Collectors.toList()),
                    ()->obj.getComments().collect(Collectors.toList()),
                    ()->obj.getRatings().collect(Collectors.toList()),
                    ()->obj.getFollowing().collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    //todo fix
    private static User prepareUpdateProcedure(CallableStatement cs, User obj){
        try {
            cs.setLong(1, obj.getIdentityKey());
            cs.setString(2, obj.getEmail());
            cs.setFloat(3, obj.getRating());
            cs.setString(4, obj.getPassword());
            cs.setString(5, obj.getName());
            cs.setString(6, obj.getSummary());
            cs.setString(7, obj.getPhotoUrl());
            cs.registerOutParameter(8, Types.BIGINT);//VERSION
            cs.execute();

            long version = cs.getLong(8);

            return new User(
                    obj.getIdentityKey(),
                    obj.getEmail(),
                    obj.getPassword(),
                    obj.getRating(),
                    version,
                    obj.getName(),
                    obj.getSummary(),
                    obj.getPhotoUrl(),
                    ()->obj.getOfferedJobs().collect(Collectors.toList()),
                    ()->obj.getCurriculums().collect(Collectors.toList()),
                    ()->obj.getApplications().collect(Collectors.toList()),
                    ()->obj.getChats().collect(Collectors.toList()),
                    ()->obj.getComments().collect(Collectors.toList()),
                    ()->obj.getRatings().collect(Collectors.toList()),
                    ()->obj.getFollowing().collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static User prepareDeleteProcedure(CallableStatement callableStatement, User obj){
        try {
            callableStatement.setLong(1, obj.getIdentityKey());
            callableStatement.execute();
            return obj;
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
