package dataMapper;

import model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class AccountMapper extends AbstractMapper<Account> {

    public AccountMapper(Map<String, Account> identityMap) {
        super(identityMap);
    }

    public Optional<Account> findByEmail(String email) throws DataMapperException {
        return findByPK(email);
    }

    @Override
    protected String findByPKStatement() {
        return "SELECT Email, Password, Rate FROM Account WHERE Email = ?";
    }

    @Override
    protected Account mapper(ResultSet rs) throws DataMapperException {
        try {
            String email = rs.getString("Email");
            String password = rs.getString("Password");
            Double rate = rs.getDouble("Rate");

            Account account = Account.load(email, password, rate);
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

    }

    @Override
    public void update(Account obj) {

    }

    @Override
    public void delete(Account obj) {

    }
}
