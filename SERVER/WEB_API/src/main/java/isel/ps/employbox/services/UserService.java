package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.User;
import javafx.util.Pair;
import org.github.isel.rapper.DataRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static isel.ps.employbox.ErrorMessages.resourceNotfound_user;

@Service
public class UserService {

    private final DataRepository<User, Long> userRepo;
    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final DataRepository<Application, Application.ApplicationKeys> applicationRepo;

    public UserService(
            DataRepository<User, Long> userRepo,
            DataRepository<Curriculum, Long> curriculumRepo,
            DataRepository<Application, Application.ApplicationKeys> applicationRepo) {
        this.userRepo = userRepo;
        this.curriculumRepo = curriculumRepo;
        this.applicationRepo = applicationRepo;
    }

    public CompletableFuture<Stream<User>> getAllUsers() {
        return userRepo.findAll()
                .thenApply(list -> list.stream());
    }

    public CompletableFuture<User> getUser(long id, String... email) {
        if (email.length > 1)
            throw new InvalidParameterException("Only 1 or 2 parameters are allowed for this method");

        return userRepo.findById(id)
                .thenApply(res -> {
                            if (!res.isPresent())
                                throw new ResourceNotFoundException(resourceNotfound_user);
                            if (email.length == 1 && !res.get().getEmail().equals(email[0]))
                                throw new UnauthorizedException(ErrorMessages.unAuthorized_IdAndEmailMismatch);
                            return res.get();
                        }
                );
    }

    public CompletableFuture<Application> getApplication(long userId, long jobId) {
        return getUser(userId)
                .thenApply(user -> user.getApplications().get())
                .thenApply(applications -> {
                    Optional<Application> application = applications.stream().filter(curr -> curr.getUserId() == userId && curr.getJobId() == jobId).findFirst();
                    if (applications.isEmpty() || !application.isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_application);
                    return application.get();
                });
    }


    public CompletableFuture<Stream<Application>> getAllApplications(long accountId)/**), Map<String, String> queryString) {*/
    {
        return userRepo.findWhere(new Pair<>("accountId", accountId))
                .thenApply(users -> users.get(0).getApplications().get().stream())
                .exceptionally(__ -> {
                    throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_user);
                });
    }


    public CompletableFuture<Stream<Curriculum>> getCurricula(long userId, String email) /**, Map<String, String> queryString) {*/
    {
        return getUser(userId, email).thenApply(
                user -> user.getCurricula()
                        .get()
                        .stream()
                        .filter(curr -> curr.getUserId() == userId)
        );
    }

    public CompletableFuture<Curriculum> getCurriculum(long userId, long cid, String email) {
        return getUser(userId, email)
                .thenApply(user -> user.getCurricula().get())
                .thenApply(curricula -> {
                    Optional<Curriculum> oret;
                    if (curricula.isEmpty() || !(oret = curricula.stream().filter(curr -> curr.getIdentityKey() == cid).findFirst()).isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_curriculum);
                    return oret.get();
                });
    }

    public Mono<Void> updateUser(User user, String email) {
        return Mono.fromFuture(
                getUser(user.getIdentityKey(), email)
                        .thenCompose(__ -> userRepo.update(user))
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                        })
        );
    }

    public Mono<Void> updateApplication(Application application, String email) {
        return Mono.fromFuture(
                getUser(application.getUserId(), email)
                        .thenCompose(__ -> getApplication(application.getUserId(), application.getJobId()))
                        .thenCompose(___ -> applicationRepo.update(application))
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                        })
        );
    }

    public Mono<Void> updateCurriculum(Curriculum curriculum, String email) {
        return Mono.fromFuture(
                getUser(curriculum.getUserId(), email)
                        .thenCompose(__ -> getCurriculum(curriculum.getUserId(), curriculum.getIdentityKey(), email))
                        .thenCompose(___ -> curriculumRepo.update(curriculum))
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                        })
        );
    }


    public CompletableFuture<User> createUser(User user) {
        return userRepo.create(user).thenApply(
                res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                    return user;
                });
    }

    public CompletableFuture<Application> createApplication(long userId, Application application, String email) {
        if (application.getUserId() != userId)
            throw new BadRequestException(ErrorMessages.badRequest_IdsMismatch);

        return getUser(userId, email)
                .thenCompose(__ -> applicationRepo.create(application))
                .thenApply(res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                    return application;
                });
    }

    public CompletableFuture<Curriculum> createCurriculum(long userId, Curriculum curriculum, String email) {
        if (curriculum.getUserId() != userId)
            throw new BadRequestException(ErrorMessages.badRequest_IdsMismatch);

        return getUser(userId, email)
                .thenCompose(__ -> curriculumRepo.create(curriculum))
                .thenApply(res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                    return curriculum;
                });
    }

    public Mono<Void> deleteUser(long id, String email) {
        return Mono.fromFuture(
                getUser(id, email)
                        .thenCompose(userRepo::delete)
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                        })
        );
    }

    public Mono<Void> deleteApplication(long userId, long jobId, String email) {
        return Mono.fromFuture(
                getUser(userId, email)
                        .thenCompose(__ -> getApplication(userId, jobId))
                        .thenCompose(applicationRepo::delete)
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                        })
        );
    }

    public Mono<Void> deleteCurriculum(long userId, long cid, String name) {
        return Mono.fromFuture(
                getCurriculum(userId, cid, name)
                        .thenCompose(curriculumRepo::delete)
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                        })
        );
    }
}