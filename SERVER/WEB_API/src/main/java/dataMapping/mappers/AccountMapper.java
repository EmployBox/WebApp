package dataMapping.mappers;

import dataMapping.exceptions.DataMapperException;
import dataMapping.utils.ConnectionManager;
import model.Account;

import java.sql.*;
import java.util.Optional;

public class AccountMapper extends AbstractMapper<Account> {
    private final String SELECT_QUERY = "SELECT AccountID, Email, passwordHash, Rating FROM Account WHERE Email = ?";
    private final String INSERT_QUERY = "INSERT INTO Account (Email, Password, Rating, Version) VALUES (?, ?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE Account SET Password = ?, Rating = ?, Version = ? WHERE AccountID = ? AND Version = ?";
    private final String DELETE_QUERY = "DELETE FROM Account WHERE AccountID = ? AND Version = ?";

    public Optional<Account> findByEmail(String email) throws DataMapperException {
        return findByPrimaryKey(email);
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
            String password = rs.getString("PasswordHash");
            Double rate = rs.getDouble("Rating");

            Account account = Account.load(accountID, email, password, rate, 0);
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
        /*DBHelper(
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
                stmt -> {
                    SQLException sqlException = null;
                    try {
                        ResultSet rs = stmt.getGeneratedKeys();
                        rs.next();
                        Account account = Account.load(rs.getLong(1), obj.getEmail(), obj.getPassword(), obj.getRating(), obj.getVersion());
                        getIdentityMap().put(account.getIdentityKey(), account);
                    } catch (SQLException e) {
                        sqlException = e;
                    }
                    return sqlException;
                }
        );*/

        /*cs = this.con.prepareCall("{call GET_SUPPLIER_OF_COFFEE(?, ?)}");
        cs.setString(1, coffeeNameArg);
        cs.registerOutParameter(2, Types.VARCHAR);
        cs.executeQuery();

        String supplierName = cs.getString(2);*/


        Connection con = ConnectionManager.getConnectionManager().getConnection();
        try{
            CallableStatement cs = con.prepareCall("{call AddAccount(?, ?, ?, ?, ?)}");
            cs.setString(1, obj.getEmail());
            cs.setDouble(2, obj.getRating());
            cs.setString(3, obj.getPassword());
            cs.registerOutParameter(4, Types.BIGINT);
            cs.registerOutParameter(5, Types.NVARCHAR);
            cs.execute();
            long accountId = cs.getLong(4);

            Account account = Account.load(accountId, obj.getEmail(), obj.getPassword(), obj.getRating(), obj.getVersion());
            getIdentityMap().put(account.getIdentityKey(), account);
        } catch (SQLException e) {
            throw new DataMapperException(e.getMessage(), e);
        }
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
