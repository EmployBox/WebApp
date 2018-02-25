package dataMapper;

import model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class AccountMapper extends AbstractMapper<Account> {

    public AccountMapper(Map<String, Account> identityMap) {
        super(identityMap);
    }

    public Optional<Account> findByEmail(String email) throws SQLException {
        return findByPK(email);
    }

    @Override
    protected String findByPKStatement() {
        return "SELECT Email, Password, Rate FROM Account WHERE Email = ?";
    }

    @Override
    protected Optional<Account> load(ResultSet rs) throws SQLException {
        if(rs.next()){
            String email = rs.getString("Email");
            String password = rs.getString("Password");
            Double rate = rs.getDouble("Rate");

            Account account = Account.load(email, password, rate);

            return Optional.of(account);
        }
        return Optional.empty();
    }

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
