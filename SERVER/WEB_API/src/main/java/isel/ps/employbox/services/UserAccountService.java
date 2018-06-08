package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.Transaction;
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
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static isel.ps.employbox.ErrorMessages.RESOURCE_NOTFOUND_USER;

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

    public CompletableFuture<CollectionPage<UserAccount>> getAllUsers(int page, int pageSize) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];

        return new Transaction(Connection.TRANSACTION_SERIALIZABLE)
                .andDo(() ->
                        userRepo.findAll(page, pageSize)
                                .thenCompose(listRes -> {
                                    list[0] = listRes;
                                    return userRepo.getNumberOfEntries();
                                })
                                .thenAccept(numberOfEntries -> ret[0] = new CollectionPage(
                                        numberOfEntries,
                                        pageSize,
                                        page,
                                        list[0]
                                ))
                ).commit()
                .thenApply(__ -> ret[0]);
    }

    public CompletableFuture<UserAccount> getUser(long id, String... email) {
        if (email.length > 1)
            throw new InvalidParameterException("Only 1 or 2 parameters are allowed for this method");

        return userRepo.findById(id)
                .thenApply(res -> {
                            if (!res.isPresent())
                                throw new ResourceNotFoundException(RESOURCE_NOTFOUND_USER);
                            if (email.length == 1 && !res.get().getEmail().equals(email[0]))
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                            return res.get();
                        }
                );
    }

    public CompletableFuture<Application> getApplication(long userId, long jobId) {
        return getUser(userId)
                .thenCompose(UserAccount::getApplications)
                .thenApply(applications -> {
                    Optional<Application> application = applications.stream().filter(curr -> curr.getAccountId() == userId && curr.getJobId() == jobId).findFirst();
                    if (applications.isEmpty() || !application.isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_APPLICATION);
                    return application.get();
                });
    }

    public CompletableFuture<CollectionPage<Application>> getAllApplications(long accountId, int page, int numberOfItems) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];

        return userRepo.findById(accountId)
                .thenApply(ouser -> ouser.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_USER)))
                .thenCompose(__ -> applicationRepo.findWhere(page, numberOfItems, new Pair<>("accountId", accountId))
                        .thenCompose(listRes -> {
                            list[0] = listRes;
                            return applicationRepo.getNumberOfEntries(new Pair<>("accountId", accountId));
                        })
                        .thenApply(collectionSize ->
                                ret[0] = new CollectionPage(
                                        collectionSize,
                                        numberOfItems,
                                        page,
                                        list[0])
                        ));
    }

    public CompletableFuture<UserAccount> createUser(UserAccount userAccount) {
        return userRepo.create(userAccount)
                .thenApply(res -> userAccount)
                .exceptionally(throwable -> {
                    throw new ConflictException(ErrorMessages.CONFLIT_USERNAME_TAKEN);
                });
    }

    public CompletableFuture<Application> createApplication(long userId, Application application, String email) {
        if (application.getAccountId() != userId)
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

        return getUser(userId, email)
                .thenCompose(userAccount -> applicationRepo.create(application))
                .thenApply(res -> application);
    }

    public Mono<Void> updateUser(UserAccount userAccount, String email) {
        return Mono.fromFuture(
                getUser(userAccount.getIdentityKey(), email)
                        .thenCompose(userAccount1 -> userRepo.update(userAccount))
        );
    }

    public Mono<Void> updateApplication(Application application, String email) {
        return Mono.fromFuture(
                getUser(application.getAccountId(), email)
                        .thenCompose(userAccount -> getApplication(application.getAccountId(), application.getJobId()))
                        .thenCompose(application1 -> applicationRepo.update(application))
        );
    }

    public Mono<Void> deleteUser(long id, String email) {
        return Mono.fromFuture(
                getUser(id, email)
                        //TODO remove entries from other tables where user has foreign key
                        .thenCompose(userAccount -> new Transaction(Connection.TRANSACTION_READ_COMMITTED)
                                .andDo(() -> userAccount.getApplications()
                                        .thenCompose(applications -> {
                                            List<Long> applicationIds = applications.stream().map(Application::getIdentityKey).collect(Collectors.toList());
                                            return applicationRepo.deleteAll(applicationIds);
                                        }))
                                .andDo(() -> userRepo.delete(userAccount))
                                .commit())
        );
    }

    public Mono<Void> deleteApplication(long userId, long jobId, String email) {
        return Mono.fromFuture(
                getUser(userId, email)
                        .thenCompose(userAccount -> getApplication(userId, jobId))
                        .thenCompose(applicationRepo::delete)
        );
    }
}