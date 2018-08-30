package isel.ps.employbox.model.binders;

import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InUserAccount;
import isel.ps.employbox.model.output.OutUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class UserBinder implements ModelBinder<UserAccount,OutUser,InUserAccount> {

    private final PasswordEncoder passwordEncoder;

    public UserBinder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CompletableFuture<OutUser> bindOutput(UserAccount userAccount) {
        return CompletableFuture.completedFuture(new OutUser(
                userAccount.getIdentityKey(),
                userAccount.getName(),
                userAccount.getEmail(),
                userAccount.getPhotoUrl(),
                userAccount.getSummary(),
                userAccount.getRating()
        ));
    }

    @Override
    public UserAccount bindInput(InUserAccount inUserAccount) {
        return new UserAccount(
                inUserAccount.getId(),
                inUserAccount.getEmail(),
                inUserAccount.getPassword() != null ? passwordEncoder.encode(inUserAccount.getPassword()) : null,
                inUserAccount.getRating(),
                inUserAccount.getName(),
                inUserAccount.getSummary(),
                inUserAccount.getPhoto_url(),
                inUserAccount.getAccountVersion(),
                inUserAccount.getUserVersion());
    }
}
