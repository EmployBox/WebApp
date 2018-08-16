package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Job;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.ErrorMessages.RESOURCE_NOTFOUND_ACCOUNT;
import static isel.ps.employbox.services.ServiceUtils.getCollectionPageFuture;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public final class AccountService {

    public CompletableFuture<CollectionPage<Account>> getAllAccounts(int page, int pageSize) {
        return ServiceUtils.getCollectionPageFuture(Account.class, page, pageSize);
    }

    public CompletableFuture<Account> getAccount(long id, String... email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);

        if (email.length > 1)
            throw new InvalidParameterException("Only 1 or 2 parameters are allowed for this method");

        CompletableFuture<Account> future = accountMapper.findById(id)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(oacc -> oacc.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOTFOUND_ACCOUNT)))
                .thenApply(account -> {
                    if (email.length == 1 && !account.getEmail().equals(email[0]))
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return account;
                });
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<CollectionPage<Job>> getOfferedJob(long accountId, int page, int pageSize){
        return getCollectionPageFuture(Job.class , page, pageSize, new EqualAndCondition<>("accountId", accountId));
    }

    public CompletableFuture<Account> getAccount(String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);

        CompletableFuture<Account> future = accountMapper.find(new EqualAndCondition<String>("email", email))
                .thenCompose(accounts -> unitOfWork.commit().thenApply(aVoid -> accounts))
                .thenApply(accounts -> {
                    if (accounts.size() != 1) throw new ResourceNotFoundException(RESOURCE_NOTFOUND_ACCOUNT);
                    return accounts.get(0);
                });
        return handleExceptions(future, unitOfWork);
    }
}
