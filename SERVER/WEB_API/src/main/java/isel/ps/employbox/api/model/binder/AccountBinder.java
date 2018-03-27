package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.ModelBinder;
import isel.ps.employbox.api.model.input.InAccount;
import isel.ps.employbox.api.model.output.OutAccount;
import isel.ps.employbox.dal.model.Account;
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
