package isel.ps.employbox.services;

import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.RapperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public final class AccountService {
    private RapperRepository<Account, Long> accountRepo;

    public AccountService(RapperRepository<Account, Long> accountRepo) {
        this.accountRepo = accountRepo;
    }

    public List<Account> getAllAccounts() {
        return null;
    }

    public Optional<Account> getAccount(long id) {
        return accountRepo.findById(id);
    }
}
