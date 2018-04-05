package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.input.InAccount;
import isel.ps.employbox.model.output.OutAccount;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AccountBinder extends ModelBinder<Account, OutAccount, InAccount> {


    @Override
    Stream<Account> bindInput(Stream<InAccount> list) {
        return null;
    }

    @Override
    Resource<OutAccount> bindOutput(Account object) {
        return null;
    }

    @Override
    Account bindInput(InAccount object) {
        return null;
    }
}
