package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static isel.ps.employbox.ErrorMessages.RESOURCE_NOTFOUND_USER;

@Service
public class UserService {

    private final DataRepository<User, Long> userRepo;
    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final DataRepository<Application, Long> applicationRepo;

    public UserService(
            DataRepository<User, Long> userRepo,
            DataRepository<Curriculum, Long> curriculumRepo,
            DataRepository<Application, Long> applicationRepo) {
        this.userRepo = userRepo;
        this.curriculumRepo = curriculumRepo;
        this.applicationRepo = applicationRepo;
    }

    public CompletableFuture<Stream<User>> getAllUsers() {
        return userRepo.findAll()
                .thenApply(Collection::stream);
    }

    public CompletableFuture<User> getUser(long id, String... email) {
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
                .thenCompose(User::getApplications)
                .thenApply(applications -> {
                    Optional<Application> application = applications.stream().filter(curr -> curr.getAccountId() == userId && curr.getJobId() == jobId).findFirst();
                    if (applications.isEmpty() || !application.isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_APPLICATION);
                    return application.get();
                });
    }


    public CompletableFuture<Stream<Application>> getAllApplications(long accountId)
    {
        return userRepo.findById(accountId)
                .thenApply( ouser -> ouser.orElseThrow( ()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_USER)))
                .thenApply(user -> user.getApplications().join().stream())
                .exceptionally(__ -> {
                    throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_USER);
                });
    }


    public Mono<Void> updateUser(User user, String email) {
        return Mono.fromFuture(
                getUser(user.getIdentityKey(), email)
                        .thenCompose(__ -> userRepo.update(user))
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                        })
        );
    }

    public Mono<Void> updateApplication(Application application, String email) {
        return Mono.fromFuture(
                getUser(application.getAccountId(), email)
                        .thenCompose(__ -> getApplication(application.getAccountId(), application.getJobId()))
                        .thenCompose(___ -> applicationRepo.update(application))
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                        })
        );
    }

    public CompletableFuture<User> createUser(User user) {
        return userRepo.create(user).thenApply(
                res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                    return user;
                }).exceptionally(e -> {
                    throw new ConflictException(ErrorMessages.CONFLIT_USERNAME_TAKEN);
                }
        );
    }

    public CompletableFuture<Application> createApplication(long userId, Application application, String email) {
        if (application.getAccountId() != userId)
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

        return getUser(userId, email)
                .thenCompose(__ -> applicationRepo.create(application))
                .thenApply(res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                    return application;
                });
    }

    public Mono<Void> deleteUser(long id, String email) {
        return Mono.fromFuture(
                getUser(id, email)
                        .thenCompose(userRepo::delete)
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                        })
        );
    }

    public Mono<Void> deleteApplication(long userId, long jobId, String email) {
        return Mono.fromFuture(
                getUser(userId, email)
                        .thenCompose(__ -> getApplication(userId, jobId))
                        .thenCompose(applicationRepo::delete)
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                        })
        );
    }
}