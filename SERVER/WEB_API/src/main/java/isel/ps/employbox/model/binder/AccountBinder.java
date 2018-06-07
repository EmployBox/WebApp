package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.input.InAccount;
import isel.ps.employbox.model.output.OutAccount;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.CompletableFuture;

@Component
public class AccountBinder implements ModelBinder<Account,OutAccount,InAccount> {


    @Override
    public Mono<OutAccount> bindOutput(CompletableFuture<Account> accountCompletableFuture) {
        return Mono.fromFuture(
                    accountCompletableFuture.thenApply(
                            account ->
                                    new OutAccount(
                                            account.getIdentityKey(),
                                            account.getName(),
                                            account.getEmail(),
                                            account.getRating(),
                                            account.getAccountType(),
                                            account.getVersion())
                    )
        );
    }

    @Override
    public Account bindInput(InAccount object) {
        throw new NotImplementedException();
    }
}
