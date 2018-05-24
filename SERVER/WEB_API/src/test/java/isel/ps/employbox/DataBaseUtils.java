package isel.ps.employbox;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.model.binder.UserBinder;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InUser;

import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;

public class DataBaseUtils {
    public static long createUsers(UserBinder userBinder, DataRepository<UserAccount, Long> userRepo, Connection con) throws SQLException {
        InUser user = new InUser();
        user.setEmail("teste@gmail.com");
        user.setPassword("password");
        user.setName("Bruno");
        UserAccount userAccount = userBinder.bindInput(user);

        assertTrue(userRepo.create(userAccount).join());

        user.setEmail("lol@hotmail.com");
        user.setPassword("teste123");
        user.setName("Maria");
        userAccount = userBinder.bindInput(user);

        assertTrue(userRepo.create(userAccount).join());

        con.commit();

        return userAccount.getIdentityKey();
    }
}
