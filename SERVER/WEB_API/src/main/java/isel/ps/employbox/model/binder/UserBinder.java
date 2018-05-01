package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.User;
import isel.ps.employbox.model.input.InUser;
import isel.ps.employbox.model.output.OutUser;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class UserBinder implements ModelBinder<User,OutUser,InUser> {

    @Override
    public Mono<OutUser> bindOutput(CompletableFuture<User> userCompletableFuture) {
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
    public User bindInput(InUser inUser) {
        return new User(
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
