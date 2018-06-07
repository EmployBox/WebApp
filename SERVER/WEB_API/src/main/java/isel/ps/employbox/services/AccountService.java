package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.RESOURCE_NOTFOUND_ACCOUNT;

@Service
public final class AccountService {
    private DataRepository<Account, Long> accountRepo;

    public AccountService(DataRepository<Account, Long> accountRepo) {
        this.accountRepo = accountRepo;
    }

    public CompletableFuture<CollectionPage<Account>> getAllAccounts(int page, int pageSize) {
        return ServiceUtils.getCollectionPageFuture(accountRepo, page, pageSize);
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
}
