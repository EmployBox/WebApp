package dataMapping.mappers;


import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.ConnectionManager;
import model.User;

import java.sql.*;
import java.util.function.Function;

//todo i must finish this
public class UserMapper extends AccountMapper<User> {
    private final String SELECT_QUERY = "SELECT AccountId, Name, Summary, PhotoUrl FROM User WHERE AccountId = ?";

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

            User user = User.load(accountID, email, passwordHash, rating, version,name ,summary, photoUrl, null , null );
            identityMap.put(accountID, user);

            return user;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    private Function<CallableStatement, SQLException> updatePrepareStatement(User obj) {
        return cs -> {
            SQLException sqlException = null;
            try {
                cs.setString(1, obj.getEmail());
                cs.setDouble(2, obj.getRating());
                cs.setString(3, obj.getPassword());
                cs.setString(4, obj.getName());
                cs.setString(5, obj.getSummary());
                cs.setString(6, obj.getPhotoUrl());
                cs.registerOutParameter(7, Types.BIGINT);
                cs.registerOutParameter(8, Types.NVARCHAR);
            } catch (SQLException e) {
                sqlException = e;
            }
            return sqlException;
        };
    }

    @Override
    public void insert(User obj) {
        executeSQLProcedure(
                "{call AddUser(?, ?, ?, ?, ?, ?, ?, ?)}",
                updatePrepareStatement(obj),
                callableStatement -> identityMap.put(obj.getIdentityKey(), obj)
        );
    }

    @Override
    public void update(User obj) {
        executeSQLProcedure(
                "{call UpdateUser(?, ?, ?, ?, ?, ?, ?, ?)}",
                updatePrepareStatement(obj),
                callableStatement -> identityMap.put(obj.getIdentityKey(), obj)
        );
    }

    @Override
    public void delete(User obj) {
        executeSQLProcedure(
                "{call DeleteUser(?, ?)}",
                callableStatement -> {
                    SQLException sqlException = null;
                    try {
                        callableStatement.setString(1, obj.getEmail());
                        callableStatement.registerOutParameter(2, Types.NVARCHAR);
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                },
                callableStatement -> identityMap.remove(obj.getIdentityKey())
        );
    }
}
