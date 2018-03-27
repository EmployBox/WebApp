package isel.ps.employbox.api.services;

import isel.ps.employbox.dal.RapperRepository;
import isel.ps.employbox.dal.model.*;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.DoubleStream;

@Service
public final class AccountService {
    private RapperRepository<Account, Long> accountRepo;

    public AccountService(RapperRepository<Account, Long> accountRepo) {
        this.accountRepo = accountRepo;
    }

    public List<Account> getAccountsPage() {
        return null;
    }

    public Optional<Account> getAccount(long id) {
        return accountRepo.findById(id);
    }
}
