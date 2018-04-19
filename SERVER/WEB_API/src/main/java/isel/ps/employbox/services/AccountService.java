package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Account;
import org.github.isel.rapper.DataRepository;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static isel.ps.employbox.ErrorMessages.resourceNotfound_account;

@Service
public final class AccountService {
    private DataRepository<Account, Long> accountRepo;

    public AccountService(DataRepository<Account, Long> accountRepo) {
        this.accountRepo = accountRepo;
    }

    public Stream<Account> getAllAccounts() {
        return StreamSupport.stream(accountRepo.findAll().join().spliterator(),false);
    }

    public Account getAccount(long id, String... email)  {
        if(email.length > 1)
            throw new InvalidParameterException("Only 1 or 2 parameters are allowed for this method");


        Optional<Account> oacc = accountRepo.findById(id).join();
        if(!oacc.isPresent())
           throw new ResourceNotFoundException( resourceNotfound_account);
        Account acc = oacc.get();

        if(email.length == 1 && !acc.getEmail().equals(email[0]))
            throw new UnauthorizedException(ErrorMessages.unAuthorized_IdAndEmailMismatch);

        return acc;
    }
}
