package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InUser;
import isel.ps.employbox.model.output.OutUser;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class UserBinder implements ModelBinder<UserAccount,OutUser,InUser> {

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
    public UserAccount bindInput(InUser inUser) {
        return new UserAccount(
                inUser.getId(),
                inUser.getEmail(),
                inUser.getPassword(),
                0,
                inUser.getName(),
                inUser.getSummary(),
                inUser.getPhoto_url(),
                inUser.getAccountVersion(),
                inUser.getUserVersion());
    }
}
