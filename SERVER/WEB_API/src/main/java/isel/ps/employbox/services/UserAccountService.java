package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.UserAccount;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.ErrorMessages.*;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class UserAccountService {


    public CompletableFuture<CollectionPage<UserAccount>> getAllUsers(int page, int pageSize, String name, Integer ratingLow, Integer ratingHigh) {
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("name", name));
        Pair[] query = pairs.stream()
                .filter(stringStringPair -> stringStringPair.getValue() != null)
                .toArray(Pair[]::new);
        return ServiceUtils.getCollectionPageFuture(UserAccount.class, page, pageSize, query);
    }

    public CompletableFuture<UserAccount> getUser(long id, String... email) {
        if (email.length > 1)
            throw new InvalidParameterException("Only 1 or 2 parameters are allowed for this method");

        UnitOfWork unit = new UnitOfWork();
        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);
        CompletableFuture<UserAccount> future = userMapper.findById(id)
                .thenApply(res -> {
                    if (!res.isPresent()) throw new ResourceNotFoundException(RESOURCE_NOTFOUND_USER);
                    if (email.length == 1 && !res.get().getEmail().equals(email[0]))
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return res.get();
                })
                .thenCompose(userAccount -> unit.commit().thenApply(aVoid -> userAccount));
        return handleExceptions(future, unit);
    }

    public CompletableFuture<Application> getApplication(long userId, long jobId, long apId) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        CompletableFuture<Application> future = getUser(userId)
                .thenCompose(ignored -> applicationMapper.findById(apId))
                .thenCompose(application1 -> unit.commit().thenApply(aVoid -> application1))
                .thenApply(oapplication -> oapplication.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOTFOUND_APPLICATION)))
                .thenApply(application -> {
                    if (application.getJobId() != jobId)
                        throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
                    return application;
                });
        return handleExceptions(future, unit);
    }

    public CompletableFuture<CollectionPage<Application>> getAllApplications(long accountId, int page, int pageSize) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        CompletableFuture<CollectionPage<Application>> future = userMapper.findById( accountId)
                .thenApply(ouser -> ouser.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_USER)))
                .thenCompose(ignored -> applicationMapper.findWhere(page, pageSize, new Pair<>("accountId", accountId))
                        .thenCompose(listRes -> applicationMapper.getNumberOfEntries(new Pair<>("accountId", accountId))
                                .thenCompose(aLong -> unit.commit().thenApply(aVoid -> aLong))
                                .thenApply(collectionSize -> new CollectionPage<>(
                                        collectionSize,
                                        pageSize,
                                        page,
                                        listRes
                                ))));
        return handleExceptions(future, unit);
    }

    public CompletableFuture<UserAccount> createUser(UserAccount userAccount) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);
        CompletableFuture<UserAccount> future = userMapper.create( userAccount)
                .thenCompose(aVoid -> unit.commit())
                .thenApply(res -> userAccount)
                .exceptionally(throwable -> {
                    throw new ConflictException(ErrorMessages.CONFLIT_USERNAME_TAKEN);
                });

        return handleExceptions(future, unit);
    }

    public CompletableFuture<Application> createApplication(long userId, Application application, String email) {
        if (application.getAccountId() != userId) throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

        UnitOfWork unit = new UnitOfWork();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        CompletableFuture<Application> future = getUser(userId, email)
                .thenCompose(userAccount -> applicationMapper.create( application))
                .thenCompose(aVoid -> unit.commit())
                .thenApply(res -> application);
        return handleExceptions(future, unit);
    }

    public Mono<Void> updateUser(UserAccount userAccount, String email) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);
        CompletableFuture<Void> future = getUser(userAccount.getIdentityKey(), email)
                .thenCompose(userAccount1 -> userMapper.update( userAccount)
                        .thenCompose(aVoid -> unit.commit()));
        return Mono.fromFuture(
                handleExceptions(future, unit)
        );
    }

    public Mono<Void> updateApplication(Application application, String email, long apId) {
        if(apId != application.getIdentityKey())
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unit = new UnitOfWork();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        CompletableFuture<Void> future = getUser(application.getAccountId(), email)
                .thenCompose(userAccount -> getApplication(application.getAccountId(), application.getJobId(), application.getIdentityKey()))
                .thenCompose(application1 -> applicationMapper.update(application)
                        .thenCompose(aVoid -> unit.commit()));
        return Mono.fromFuture(
                handleExceptions(future, unit)
        );
    }

    public Mono<Void> deleteUser(long id, String email) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);
        CompletableFuture<Void> future = getUser(id, email)
                //TODO remove entries from other tables where user has foreign key
                .thenCompose(userAccount -> userAccount.getApplications().apply(unit)
                        .thenCompose(applications -> {
                            List<Long> applicationIds = applications.stream().map(Application::getIdentityKey).collect(Collectors.toList());
                            return applicationMapper.deleteAll( applicationIds);
                        })
                        .thenCompose(aVoid -> userMapper.delete(userAccount))
                        .thenCompose(aVoid -> unit.commit()));
        return Mono.fromFuture(
                handleExceptions(future, unit)
        );
    }

    public Mono<Void> deleteApplication(long userId, long jobId,long apId, String email) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        CompletableFuture<Void> future = getUser(userId, email)
                .thenCompose(userAccount -> getApplication(userId, jobId, apId))
                .thenCompose(application -> applicationMapper.delete(application))
                .thenCompose(aVoid -> unit.commit());
        return Mono.fromFuture(
                handleExceptions(future, unit)
        );
    }
}