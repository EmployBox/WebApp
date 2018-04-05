package isel.ps.employbox.services;

import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.model.entities.Account;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public final class AccountService {
    private RapperRepository<Account, Long> accountRepo;

    public AccountService(RapperRepository<Account, Long> accountRepo) {
        this.accountRepo = accountRepo;
    }

    public Stream<Account> getAllAccounts() {
        return null;
    }

    public Account getAccount(long id) {
        //return accountRepo.findById(id);
        return null;
    }
}
