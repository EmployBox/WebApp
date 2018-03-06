package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.ConnectionManager;
import dataMapping.utils.MapperRegistry;
import model.Curriculum;
import model.Job;
import model.User;
import util.Streamable;

import java.sql.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class UserMapper extends AccountMapper<User> {
    private final String SELECT_QUERY = "SELECT a.email, a.passwordHash, a.rating, u.accountId, u.name, u.summary, u.PhotoUrl, u.[version]\n" +
            "FROM ApiDatabase.[User] u inner join ApiDatabase.Account a\n" +
            "ON u.accountId = a.accountId AND a.accountId = ?";

    @Override
    public User mapper(ResultSet rs) throws DataMapperException {
        try {
            long accountID = rs.getLong("AccountID");
            String email = rs.getString("Email");
            String passwordHash = rs.getString("PasswordHash");
            Double rating = rs.getDouble("Rating");
            String name = rs.getString ("Name");
            String summary = rs.getString ("Summary");
            String photoUrl = rs.getString ("PhotoUrl");
            long version = rs.getLong("[version]");

            Streamable<Job> offeredJobs = ((JobMapper) MapperRegistry.getMapper(Job.class)).findForAccount(accountID);

            User user = User.load(accountID, email, passwordHash, rating, version,name ,summary, photoUrl, offeredJobs, null, null);
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

    private Consumer<CallableStatement> updatePrepareStatement(User obj) {
        return cs -> {
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
                identityMap.put(obj.getIdentityKey(), obj);
            } catch (SQLException e) {
                throw new DataMapperException(e);
            }
        };
    }

    @Override
    public void insert(User obj) {
        executeSQLProcedure(
                "{call AddUser(?, ?, ?, ?, ?, ?, ?, ?)}",
                updatePrepareStatement(obj)
        );
    }

    @Override
    public void update(User obj) {
        executeSQLProcedure(
                "{call UpdateUser(?, ?, ?, ?, ?, ?, ?, ?)}",
                updatePrepareStatement(obj)
        );
    }

    @Override
    public void delete(User obj) {
        executeSQLProcedure(
                "{call DeleteUser(?, ?)}",
                callableStatement -> {
                    try {
                        callableStatement.setString(1, obj.getEmail());
                        callableStatement.registerOutParameter(2, Types.NVARCHAR);
                        callableStatement.execute();
                        identityMap.remove(obj.getIdentityKey());
                    } catch (SQLException e) {
                        throw new DataMapperException(e);
                    }
                }
        );
    }
}
