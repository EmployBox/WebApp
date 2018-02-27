package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountMapper extends AbstractMapper<Account> {
    private final String SELECT_QUERY = "SELECT AccountID, Email, Password, Rating FROM Account WHERE AccountID = ?";
    private final String INSERT_QUERY = "INSERT INTO Account (Email, Password, Rating, Version) VALUES (?, ?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE Account SET Password = ?, Rating = ?, Version = ? WHERE AccountID = ? AND Version = ?";
    private final String DELETE_QUERY = "DELETE FROM Account WHERE AccountID = ? AND Version = ?";

    public Optional<Account> findByEmail(String email) throws DataMapperException {
        return findByPK(email);
    }

    @Override
    protected String findByPKStatement() {
        return SELECT_QUERY;
    }

    @Override
    public Account mapper(ResultSet rs) throws DataMapperException {
        try {
            long accountID = rs.getLong("AccountID");
            String email = rs.getString("Email");
            String password = rs.getString("Password");
            Double rate = rs.getDouble("Rating");
            long version = rs.getLong("Version");

            Account account = Account.load(accountID, email, password, rate, version);
            getIdentityMap().put(email, account);

            return account;
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
    }

//    private Account load(ResultSet rs) throws DataMapperException {
//        try {
//                String email = rs.getString("Email");
//                String password = rs.getString("Password");
//                Double rate = rs.getDouble("Rate");
//
//                Account account = Account.load(email, password, rate);
//                getIdentityMap().put(email, account);
//
//                return account;
//        } catch (SQLException e) {
//            throw new DataMapperException(e.getMessage(), e);
//        }
//    }

    @Override
    public void insert(Account obj) {
        DBHelper(
                INSERT_QUERY,
                stmt -> {
                    SQLException sqlException = null;
                    try {
                        stmt.setString(1, obj.getEmail());
                        stmt.setString(2, obj.getPassword());
                        stmt.setDouble(3, obj.getRating());
                        stmt.setLong(4, obj.getVersion());
                    } catch (SQLException e) {
                       sqlException = e;
                    }
                    return sqlException;
                },
                () -> getIdentityMap().put(obj.getIdentityKey(), obj)
        );
    }

    @Override
    public void update(Account obj) {
        DBHelper(
                UPDATE_QUERY,
                stmt -> {
                    SQLException sqlException = null;
                    try{
                        stmt.setString(1, obj.getPassword());
                        stmt.setDouble(2, obj.getRating());
                        stmt.setLong(3, obj.getVersion());
                        stmt.setLong(4, obj.getAccountID());
                        stmt.setLong(5, obj.getVersion()-1);
                    }catch (SQLException e){
                        sqlException = e;
                    }
                    return sqlException;
                },
                () -> getIdentityMap().put(obj.getIdentityKey(), obj)
        );
    }

    @Override
    public void delete(Account obj) {
        DBHelper(
                DELETE_QUERY,
                stmt -> {
                    SQLException sqlException = null;
                    try{
                        stmt.setLong(1, obj.getAccountID());
                        stmt.setLong(2, obj.getVersion());
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                },
                () -> getIdentityMap().remove(obj.getIdentityKey())
        );
    }
}
