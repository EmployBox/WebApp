package isel.ps.employbox.services;

import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Account;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static isel.ps.employbox.ErrorMessages.resourceNotfound_account;

@Service
public final class AccountService {
    private RapperRepository<Account, Long> accountRepo;

    public AccountService(RapperRepository<Account, Long> accountRepo) {
        this.accountRepo = accountRepo;
    }

    public Stream<Account> getAllAccounts() {
        return StreamSupport.stream(accountRepo.findAll().spliterator(),false);
    }

    public Account getAccount(long id) {
       Optional<Account> oacc = accountRepo.findById(id);
       if(!oacc.isPresent())
           throw new ResourceNotFoundException( resourceNotfound_account);

       return oacc.get();
    }
}
