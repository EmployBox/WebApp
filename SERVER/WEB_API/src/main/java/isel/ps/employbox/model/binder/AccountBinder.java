package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.ModelBinder;
import isel.ps.employbox.model.input.InAccount;
import isel.ps.employbox.model.output.OutAccount;
import isel.ps.employbox.model.entities.Account;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountBinder implements ModelBinder<Account, OutAccount, InAccount, Long> {

    @Override
    public List<OutAccount> bindOutput(List<Account> list) {
        return null;
    }

    @Override
    public List<Account> bindInput(List<InAccount> list) {
        return null;
    }

    @Override
    public OutAccount bindOutput(Account object) {
        return null;
    }

    @Override
    public Account bindInput(InAccount object) {
        return null;
    }
}
