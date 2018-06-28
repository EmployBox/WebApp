package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
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

import static isel.ps.employbox.ErrorMessages.*;

@Service
public class UserAccountService {

    private final DataRepository<UserAccount, Long> userRepo;
    private final DataRepository<Application, Long> applicationRepo;

    public UserAccountService(
            DataRepository<UserAccount, Long> userRepo,
            DataRepository<Application, Long> applicationRepo
    ) {
        this.userRepo = userRepo;
        this.applicationRepo = applicationRepo;
    }

    public CompletableFuture<CollectionPage<UserAccount>> getAllUsers(int page, int pageSize, String name, Integer ratingLow, Integer ratingHigh) {
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("name", name));
        Pair[] query = pairs.stream()
                .filter(stringStringPair -> stringStringPair.getValue() != null)
                .toArray(Pair[]::new);
        return ServiceUtils.getCollectionPageFuture(
                userRepo,
                page,
                pageSize,
                query
        );
    }

    public CompletableFuture<UserAccount> getUser(long id, String... email) {
        if (email.length > 1)
            throw new InvalidParameterException("Only 1 or 2 parameters are allowed for this method");

        UnitOfWork unit = new UnitOfWork();
        return userRepo.findById(unit, id)
                .thenApply(res -> {
                    if (!res.isPresent()) throw new ResourceNotFoundException(RESOURCE_NOTFOUND_USER);
                    if (email.length == 1 && !res.get().getEmail().equals(email[0])) throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return res.get();
                })
                .thenCompose(userAccount -> unit.commit().thenApply(aVoid -> userAccount));
    }

    public CompletableFuture<Application> getApplication(long userId, long jobId, long apId) {
        UnitOfWork unit = new UnitOfWork();
        return getUser(userId)
                .thenCompose(__ -> applicationRepo.findById(unit, apId))
                .thenCompose(application1 -> unit.commit().thenApply(aVoid -> application1))
                .thenApply( oapplication -> oapplication.orElseThrow( () -> new ResourceNotFoundException( RESOURCE_NOTFOUND_APPLICATION)))
                .thenApply( application -> {
                    if(application.getJobId() != jobId)
                        throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
                    return application;
                });
    }

    public CompletableFuture<CollectionPage<Application>> getAllApplications(long accountId, int page, int pageSize) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];
        UnitOfWork unit = new UnitOfWork();

        return userRepo.findById(unit, accountId)
                .thenApply(ouser -> ouser.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_USER)))
                .thenCompose(__ -> applicationRepo.findWhere(unit, page, pageSize, new Pair<>("accountId", accountId))
                        .thenCompose(listRes -> {
                            list[0] = listRes;
                            return applicationRepo.getNumberOfEntries(unit, new Pair<>("accountId", accountId));
                        })
                        .thenCompose(aLong -> unit.commit().thenApply(aVoid -> aLong))
                        .thenApply(collectionSize ->
                                ret[0] = new CollectionPage(
                                        collectionSize,
                                        pageSize,
                                        page,
                                        list[0])
                        ));
    }

    public CompletableFuture<UserAccount> createUser(UserAccount userAccount) {
        UnitOfWork unit = new UnitOfWork();
        return userRepo.create(unit, userAccount)
                .thenCompose(aVoid -> unit.commit())
                .thenApply(res -> userAccount)
                .exceptionally(throwable -> {
                    throw new ConflictException(ErrorMessages.CONFLIT_USERNAME_TAKEN);
                });
    }

    public CompletableFuture<Application> createApplication(long userId, Application application, String email) {
        if (application.getAccountId() != userId) throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

        UnitOfWork unit = new UnitOfWork();
        return getUser(userId, email)
                .thenCompose(userAccount -> applicationRepo.create(unit, application))
                .thenCompose(aVoid -> unit.commit())
                .thenApply(res -> application);
    }

    public Mono<Void> updateUser(UserAccount userAccount, String email) {
        UnitOfWork unit = new UnitOfWork();
        return Mono.fromFuture(
                getUser(userAccount.getIdentityKey(), email)
                        .thenCompose(userAccount1 -> userRepo.update(unit, userAccount)
                        .thenCompose(aVoid -> unit.commit()))
        );
    }

    public Mono<Void> updateApplication(Application application, String email, long apId) {
        if(apId != application.getIdentityKey())
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unit = new UnitOfWork();
        return Mono.fromFuture(
                getUser(application.getAccountId(), email)
                        .thenCompose(userAccount -> getApplication(application.getAccountId(), application.getJobId(), application.getIdentityKey()))
                        .thenCompose(application1 -> applicationRepo.update(unit, application)
                        .thenCompose(aVoid -> unit.commit()))
        );
    }

    public Mono<Void> deleteUser(long id, String email) {
        UnitOfWork unit = new UnitOfWork();
        return Mono.fromFuture(
                getUser(id, email)
                        //TODO remove entries from other tables where user has foreign key
                        .thenCompose(userAccount -> userAccount.getApplications()
                                .thenCompose(applications -> {
                                    List<Long> applicationIds = applications.stream().map(Application::getIdentityKey).collect(Collectors.toList());
                                    return applicationRepo.deleteAll(unit, applicationIds);
                                })
                                .thenCompose(aVoid -> userRepo.delete(unit, userAccount))
                                .thenCompose(aVoid -> unit.commit()))
        );
    }

    public Mono<Void> deleteApplication(long userId, long jobId,long apId, String email) {
        UnitOfWork unit = new UnitOfWork();
        return Mono.fromFuture(
                getUser(userId, email)
                        .thenCompose(userAccount -> getApplication(userId, jobId, apId))
                        .thenCompose(application -> applicationRepo.delete(unit, application))
                        .thenCompose(aVoid -> unit.commit())
        );
    }
}