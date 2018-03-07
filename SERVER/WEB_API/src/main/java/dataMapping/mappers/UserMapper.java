package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.MapperRegistry;
import dataMapping.utils.MapperSettings;
import model.Curriculum;
import model.Job;
import model.User;
import util.Streamable;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.function.Consumer;

public class UserMapper extends AccountMapper<User> {
    private static final String SELECT_QUERY = "SELECT accountId, name, summary, photoUrl, [version] FROM [User]";

    public UserMapper() {
        super(
                new MapperSettings<>("{call AddUser(?, ?, ?, ?, ?, ?, ?, ?)}", CallableStatement.class, UserMapper::prepareUpdateProcedureArguments),
                new MapperSettings<>("{call UpdateUser(?, ?, ?, ?, ?, ?, ?, ?)}", CallableStatement.class, UserMapper::prepareUpdateProcedureArguments),
                new MapperSettings<>("{call DeleteUser(?, ?)}", CallableStatement.class, UserMapper::prepareDeleteProcedure)
        );
    }

    @Override
    public User mapper(ResultSet rs) throws DataMapperException {
        try {
            long accountID = rs.getLong("AccountID");
            String email = rs.getString("Email");
            String passwordHash = rs.getString("passwordHash");
            Double rating = rs.getDouble("Rating");
            String name = rs.getString ("Name");
            String summary = rs.getString ("Summary");
            String photoUrl = rs.getString ("PhotoUrl");
            long version = rs.getLong("[version]");

            Streamable<Job> offeredJobs = ((JobMapper) MapperRegistry.getMapper(Job.class)).findForAccount(accountID);
            Streamable<Curriculum> curriculums = ((CurriculumMapper) MapperRegistry.getMapper(Curriculum.class)).findCurriculumsForAccount(accountID);

            //todo finders for comments, ratings and chats
            User user = User.load(accountID, email, passwordHash, rating, version,name ,summary, photoUrl, offeredJobs, curriculums, null, null, null , null);
            identityMap.put(accountID, user);

            return user;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    String getSelectQuery() {
        return SELECT_QUERY;
    }


    private static void prepareUpdateProcedureArguments(CallableStatement cs, User obj) {
        try {
            cs.setString(1, obj.getEmail());
            cs.setDouble(2, obj.getRating());
            cs.setString(3, obj.getPassword());
            cs.setString(4, obj.getName());
            cs.setString(5, obj.getSummary());
            cs.setString(6, obj.getPhotoUrl());
            cs.registerOutParameter(7, Types.BIGINT);
            cs.registerOutParameter(8, Types.NVARCHAR);
            cs.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }

    private static void prepareDeleteProcedure(CallableStatement callableStatement, User obj){
        try {
            callableStatement.setString(1, obj.getEmail());
            callableStatement.registerOutParameter(2, Types.NVARCHAR);
            callableStatement.execute();
        } catch (SQLException e) {
            throw new DataMapperException(e);
        }
    }
}
