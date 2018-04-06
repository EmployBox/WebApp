package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.input.InAccount;
import isel.ps.employbox.model.output.OutAccount;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Component
public class AccountBinder extends ModelBinder<Account, OutAccount, InAccount> {


    @Override
    public Resource<OutAccount> bindOutput(Account object) {
        return new Resource(
                new OutAccount(
                    object.getAccountID(),
                    object.getEmail(),
                    object.getRating())
        );
    }

    @Override
    public Account bindInput(InAccount object) {
        throw new NotImplementedException();
    }
}
