package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.jobs.Job;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.ErrorMessages.RESOURCE_NOTFOUND_ACCOUNT;
import static isel.ps.employbox.services.ServiceUtils.getCollectionPageFuture;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public final class AccountService {

    public CompletableFuture<CollectionPage<Account>> getAllAccounts(int page, int pageSize, String orderColumn, String orderClause) {
        List<Condition> conditions = new ArrayList<>();
        ServiceUtils.evaluateOrderClause(orderColumn, orderClause, conditions);
        return ServiceUtils.getCollectionPageFuture(Account.class, page, pageSize, conditions.toArray(new Condition[conditions.size()]));
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
                    if (email.length == 1 && account.getEmail().compareTo(email[0])!=0)
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return account;
                });
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<Account> getAccount(String email, UnitOfWork... unitOfWorkArr) {
        UnitOfWork unitOfWork;
        if(unitOfWorkArr.length == 0)
         unitOfWork = new UnitOfWork();
        else
            unitOfWork = unitOfWorkArr[0];

        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);

        CompletableFuture<Account> future = accountMapper.find(new EqualAndCondition<String>("email", email))
                .thenCompose(accounts -> unitOfWork.commit().thenApply(aVoid -> accounts))
                .thenApply(accounts -> {
                    if (accounts.size() != 1) throw new ResourceNotFoundException(RESOURCE_NOTFOUND_ACCOUNT);
                    return accounts.get(0);
                });
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<CollectionPage<Job>> getOfferedJob(long accountId, int page, int pageSize, String orderColumn, String orderClause){
        List<Condition> conditions = new ArrayList<>();
        ServiceUtils.evaluateOrderClause(orderColumn, orderClause, conditions);
        conditions.add(new EqualAndCondition<>("accountId", accountId));
        return getCollectionPageFuture(Job.class , page, pageSize, conditions.toArray(new Condition[conditions.size()]));
    }
}
