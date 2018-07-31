package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.utils.ConnectionManager;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.SqlSupplier;
import com.github.jayield.rapper.utils.UnitOfWork;
import io.vertx.ext.sql.TransactionIsolation;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.entities.curricula.childs.*;
import isel.ps.employbox.services.ServiceUtils;
import isel.ps.employbox.services.UserAccountService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class CurriculumService {
    private final UserAccountService userAccountService;

    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final DataRepository<PreviousJobs, Long> previousJobsRepo;
    private final DataRepository<AcademicBackground, Long> academicBackgroundRepo;
    private final DataRepository<Project, Long> projectRepo;
    private final DataRepository<CurriculumExperience, Long> curriculumExperienceRepo;

    public CurriculumService(
            UserAccountService userAccountService,
            DataRepository<Curriculum, Long> curriculumRepo,
            DataRepository<PreviousJobs, Long> previousJobsRepo,
            DataRepository<AcademicBackground, Long> academicBackgroundRepo,
            DataRepository<Project, Long> projectRepo,
            DataRepository<CurriculumExperience, Long> curriculumExperienceRepo
    ) {
        this.userAccountService = userAccountService;
        this.curriculumRepo = curriculumRepo;
        this.previousJobsRepo = previousJobsRepo;
        this.academicBackgroundRepo = academicBackgroundRepo;
        this.projectRepo = projectRepo;
        this.curriculumExperienceRepo = curriculumExperienceRepo;
    }

    public CompletableFuture<Curriculum> createCurriculum(long userId, Curriculum curriculum, String email) {
        if (curriculum.getAccountId() != userId)
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unitOfWork = new UnitOfWork(TransactionIsolation.SERIALIZABLE);
        CompletableFuture<Curriculum> future = userAccountService.getUser(userId, email)
                .thenCompose(userAccount -> curriculumRepo.create(unitOfWork, curriculum)
                        .thenCompose(aVoid -> {
                            List<CompletableFuture<Void>> list = new ArrayList<>();
                            populateChildList(list, curriculum, userId);
                            return CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));
                        })
                        .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> curriculum)));
        return handleExceptions(future, unitOfWork);
    }

    private <T extends CurriculumChild & DomainObject<K>, K> CompletableFuture<Void> addChildFutureFunction(
            Function<UnitOfWork, CompletableFuture<List<T>>> function,
            DataRepository<T, K> repo,
            Curriculum curriculum,
            long userId
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();

        CompletableFuture<Void> future1 = function.apply(unitOfWork)
                .thenApply(tList -> {
                    tList.forEach(t -> {
                        t.setAccountId(userId);
                        t.setCurriculumId(curriculum.getIdentityKey());
                    });
                    return tList;
                })
                .thenCompose(list -> {
                    if (list.isEmpty()) return CompletableFuture.completedFuture(null);
                    return repo.createAll(unitOfWork, list);
                })
                .thenCompose(aVoid -> unitOfWork.commit());

        return handleExceptions(future1, unitOfWork);
    }

    private void populateChildList(List<CompletableFuture<Void>> creationList, Curriculum curriculum, long userId) {
        creationList.add(addChildFutureFunction(curriculum.getPreviousJobs(), previousJobsRepo, curriculum, userId));
        creationList.add(addChildFutureFunction(curriculum.getAcademicBackground(), academicBackgroundRepo, curriculum, userId));
        creationList.add(addChildFutureFunction(curriculum.getProjects(), projectRepo, curriculum, userId));
        creationList.add(addChildFutureFunction(curriculum.getExperiences(), curriculumExperienceRepo, curriculum, userId));
    }

    public CompletableFuture<CollectionPage<Curriculum>> getCurricula(long accountId, int page, int pageSize) {
        return userAccountService.getUser(accountId)
                .thenCompose(userAccount -> ServiceUtils.getCollectionPageFuture(curriculumRepo, page, pageSize, new Pair<>("accountId", accountId)));
    }

    public CompletableFuture<Curriculum> getCurriculum(long userId, long cid, String... email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<Curriculum> future = userAccountService.getUser(userId, email)
                .thenCompose((userAccount) -> userAccount.getCurricula().apply(unitOfWork))
                .thenCompose(res -> unitOfWork.commit().thenApply((__) -> res))
                .thenApply(curricula -> {
                    Optional<Curriculum> optionalCurriculum = curricula.stream().filter(curr -> curr.getIdentityKey() == cid).findFirst();
                    if (curricula.isEmpty() || !optionalCurriculum.isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM);
                    return optionalCurriculum.get();
                });
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateCurriculum(Curriculum curriculum, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<Void> future = userAccountService.getUser(curriculum.getAccountId(), email)
                .thenCompose(userAccount -> getCurriculum(curriculum.getAccountId(), curriculum.getIdentityKey(), email))
                .thenCompose(curriculum1 -> curriculumRepo.update(unitOfWork, curriculum))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteCurriculum(long userId, long cid, String email) {
        List<Curriculum> curriculum = new ArrayList<>(1);
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<Void> future = getCurriculum(userId, cid, email)
                .thenAccept(curriculum1 -> curriculum.add(0, curriculum1))
                .thenCompose(aVoid ->
                        curriculum.get(0).getAcademicBackground().apply(unitOfWork)
                                .thenApply(academicBackgrounds -> academicBackgrounds.stream().map(AcademicBackground::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(keys -> academicBackgroundRepo.deleteAll(unitOfWork, keys))

                ).thenCompose(aVoid ->
                        curriculum.get(0).getProjects().apply(unitOfWork)
                                .thenApply(projects -> projects.stream().map(Project::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(keys -> projectRepo.deleteAll(unitOfWork, keys)))
                .thenCompose(aVoid ->
                        curriculum.get(0).getPreviousJobs().apply(unitOfWork)
                                .thenApply(previousJobs -> previousJobs.stream().map(PreviousJobs::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(keys -> previousJobsRepo.deleteAll(unitOfWork, keys))
                )
                .thenCompose(aVoid ->
                        curriculum.get(0).getExperiences().apply(unitOfWork)
                                .thenApply(curriculumExperiences -> curriculumExperiences.stream().map(CurriculumExperience::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(keys -> curriculumExperienceRepo.deleteAll(unitOfWork, keys))
                )
                .thenCompose(aVoid -> curriculumRepo.deleteById(unitOfWork, cid))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public <T extends CurriculumChild & DomainObject<Long>> CompletableFuture<T> getCurriculumChild(
            DataRepository<T, Long> repo,
            long accountId,
            long curriculumId,
            long jexpId
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<T> future = repo.findById(unitOfWork, jexpId)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(oChild -> oChild.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND)))
                .thenApply(child -> {
                    if (child.getCurriculumId() != curriculumId || child.getAccountId() != accountId)
                        throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                    return child;
                });
        return handleExceptions(future, unitOfWork);
    }
}
