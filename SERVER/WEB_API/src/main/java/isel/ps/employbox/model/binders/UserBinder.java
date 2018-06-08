package isel.ps.employbox.model.binders;

import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InUserAccount;
import isel.ps.employbox.model.output.OutUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class UserBinder implements ModelBinder<UserAccount,OutUser,InUserAccount> {

    private final PasswordEncoder passwordEncoder;

    public UserBinder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<OutUser> bindOutput(CompletableFuture<UserAccount> userCompletableFuture) {
        return Mono.fromFuture(
                userCompletableFuture.thenApply(
                        user -> new OutUser(
                                user.getIdentityKey(),
                                user.getName(),
                                user.getEmail(),
                                user.getPhotoUrl(),
                                user.getSummary(),
                                user.getRating())
                )
        );
    }

    @Override
    public UserAccount bindInput(InUserAccount inUserAccount) {
        return new UserAccount(
                inUserAccount.getId(),
                inUserAccount.getEmail(),
                inUserAccount.getPassword() != null ? passwordEncoder.encode(inUserAccount.getPassword()) : null,
                0,
                inUserAccount.getName(),
                inUserAccount.getSummary(),
                inUserAccount.getPhoto_url(),
                inUserAccount.getAccountVersion(),
                inUserAccount.getUserVersion());
    }
}
