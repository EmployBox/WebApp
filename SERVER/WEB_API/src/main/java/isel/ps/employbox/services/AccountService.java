package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Account;
import javafx.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static isel.ps.employbox.ErrorMessages.RESOURCE_NOTFOUND_ACCOUNT;
import static isel.ps.employbox.ErrorMessages.RESOURCE_NOTFOUND_USER;

@Service
public final class AccountService implements UserDetailsService {
    private DataRepository<Account, Long> accountRepo;

    public AccountService(DataRepository<Account, Long> accountRepo) {
        this.accountRepo = accountRepo;
    }


    public CompletableFuture<Stream<Account>> getAllAccounts() {
        return accountRepo.findAll().thenApply(Collection::stream);
    }

    public CompletableFuture<Account> getAccount(long id, String... email) {
        if (email.length > 1)
            throw new InvalidParameterException("Only 1 or 2 parameters are allowed for this method");

        return accountRepo.findById(id)
                .thenApply(oacc -> oacc.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOTFOUND_ACCOUNT)))
                .thenApply(account -> {
                    if (email.length == 1 && !account.getEmail().equals(email[0]))
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return account;
                });
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Account> oacc=
                accountRepo.findWhere(new Pair<>("email", email)).join()
                        .stream()
                        .filter(curr-> curr.getEmail().equals(email))
                        .findFirst();

        if( !oacc.isPresent())
            throw new UsernameNotFoundException(RESOURCE_NOTFOUND_USER);

        return oacc.get();
    }
}
