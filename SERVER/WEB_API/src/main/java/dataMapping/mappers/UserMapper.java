package dataMapping.mappers;


import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.ConnectionManager;
import model.DomainObject;
import model.User;

import java.sql.*;

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

            User user = User.load(accountID, email, passwordHash, rating, version,name ,summary, photoUrl, null, null );
            getIdentityMap().put(accountID, user);

            return user;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    public void insert(User obj) {
        writeUser(obj,"{call AddUser(?, ?, ?, ?, ?, ?, ?, ?)}");
    }

    @Override
    public void update(User obj) {
        writeUser(obj,"{call UpdateUser(?, ?, ?, ?, ?, ?, ?, ?)}");
    }

    private  void writeUser(User obj,String procedureToUse){
        Connection con = ConnectionManager.getConnectionManager().getConnection();
        try{
            CallableStatement cs = con.prepareCall(procedureToUse);
            cs.setString(1, obj.getEmail());
            cs.setDouble(2, obj.getRating());
            cs.setString(3, obj.getPassword());
            cs.setString( 4, obj.getName());
            cs.setString(5, obj.getSummary());
            cs.setString(6, obj.getPhotoUrl());
            cs.registerOutParameter(7, Types.BIGINT);
            cs.registerOutParameter(8, Types.NVARCHAR);

            cs.execute();

            getIdentityMap().put(obj.getIdentityKey(), obj);
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(User obj) {
        Connection con = ConnectionManager.getConnectionManager().getConnection();
        try{
            CallableStatement cs = con.prepareCall("{call DeleteUser(?, ?)}");
            cs.setString(1, obj.getEmail());
            cs.registerOutParameter(2, Types.NVARCHAR);

            cs.execute();

            getIdentityMap().remove(obj.getIdentityKey());

        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }
}
