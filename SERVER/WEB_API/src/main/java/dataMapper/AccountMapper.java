package dataMapper;

import model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class AccountMapper extends AbstractMapper<Account> {

    public AccountMapper(ConcurrentMap<String, Account> identityMap) {
        super(identityMap);
    }

    public Optional<Account> findByEmail(String email) throws DataMapperException {
        return findByPK(email);
    }

    @Override
    protected String findByPKStatement() {
        return "SELECT AccountID, Email, Password, Rating FROM Account WHERE AccountID = ?";
    }

    @Override
    protected Account mapper(ResultSet rs) throws DataMapperException {
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
    public void insert(Account obj) throws DataMapperException {
        Connection conn = null;
        PreparedStatement stmt;
        try {
            //conn = ConnectionManager.INSTANCE.getConnection();
            stmt = conn.prepareStatement("INSERT INTO Account (Email, Password, Rating, Version)\n" +
                    "VALUES (?, ?, ?, ?)");
            stmt.setString(1, obj.getEmail());
            stmt.setString(2, obj.getPassword());
            stmt.setDouble(3, obj.getRate());
            stmt.setLong(4, obj.getVersion());
            int rowCount = stmt.executeUpdate();
            if (rowCount == 0) {
                throw new ConcurrencyException("Concurrency on deleting the Account with Email being " + obj.getEmail());
            }
            else {
                getIdentityMap().put(obj.getIdentityKey(), obj);
            }
        } catch (SQLException e) {
            throw new DataMapperException("unexpected error inserting", e);
        }
    }

    @Override
    public void update(Account obj) throws DataMapperException, ConcurrencyException{
        Connection conn = null;
        PreparedStatement stmt;
        try {
            //conn = ConnectionManager.INSTANCE.getConnection();
            stmt = conn.prepareStatement("UPDATE Account SET password = ?, Rating = ?, Version = ? where AccountID = ? AND Version = ?");
            stmt.setString(1, obj.getPassword());
            stmt.setDouble(2, obj.getRate());
            stmt.setLong(3, obj.getVersion());
            stmt.setLong(4, obj.getAccountID());
            stmt.setLong(5, obj.getVersion()-1);
            int rowCount = stmt.executeUpdate();
            if (rowCount == 0) {
                throw new ConcurrencyException("Concurrency on deleting the Account with Email being " + obj.getEmail());
            }
            else {
                getIdentityMap().put(obj.getIdentityKey(), obj);
            }
        } catch (SQLException e) {
            throw new DataMapperException("unexpected error updating", e);
        }
    }

    @Override
    public void delete(Account obj) throws DataMapperException, ConcurrencyException{
        Connection conn = null;
        PreparedStatement stmt;
        try {
            //conn = ConnectionManager.INSTANCE.getConnection();
            stmt = conn.prepareStatement("DELETE FROM Account where Email = ? AND Version = ?");
            stmt.setString(1, obj.getEmail());
            stmt.setLong(2, obj.getVersion());
            int rowCount = stmt.executeUpdate();
            if (rowCount == 0) {
                throw new ConcurrencyException("Concurrency on deleting the Account with Email being " + obj.getEmail());
            }
            else {
                getIdentityMap().remove(obj.getIdentityKey());
            }
        } catch (SQLException e) {
            throw new DataMapperException("unexpected error deleting", e);
        }
    }
}
