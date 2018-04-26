package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.input.InAccount;
import isel.ps.employbox.model.output.OutAccount;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Component
public class AccountBinder extends ModelBinder<Account, OutAccount, InAccount> {


    @Override
    public OutAccount bindOutput(Account acount) {
        return new OutAccount(
                    acount.getIdentityKey(),
                    acount.getEmail(),
                    acount.getRating(),
                    acount.getVersion()
        );
    }

    @Override
    public Account bindInput(InAccount object) {
        throw new NotImplementedException();
    }
}
