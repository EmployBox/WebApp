package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.CurriculumChilds.*;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.services.ServiceUtils;
import isel.ps.employbox.services.UserAccountService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

        return userAccountService.getUser(userId, email)
                .thenCompose(userAccount -> curriculumRepo.create(curriculum))
                .thenApply(res -> curriculum)
                .thenCompose(curriculum1 -> {
                    List<CompletableFuture<Void>> list = new ArrayList<>();
                    populateChildList(list, curriculum, userId);
                    return CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));
                })
                .thenApply(res -> curriculum);
    }

    private <T extends CurriculumChild & DomainObject<K>, K> CompletableFuture<Void> addChildFutureFunction(
            CompletableFuture<List<T>> future,
            DataRepository<T, K> repo,
            Curriculum curriculum,
            long userId) {

        return future.thenApply(list -> {
            list.forEach(curr -> {
                curr.setAccountId(userId);
                curr.setCurriculumId(curriculum.getIdentityKey());
            });
            return list;
        })
                .thenCompose(list -> {
                    if (list.isEmpty()) return CompletableFuture.completedFuture(null);
                    return repo.createAll(list);
                });
    }

    private void populateChildList(List<CompletableFuture<Void>> creationList, Curriculum curriculum, long userId) {
        creationList.add(addChildFutureFunction(curriculum.getPreviousJobs(), previousJobsRepo, curriculum, userId));
        creationList.add(addChildFutureFunction(curriculum.getAcademicBackground(), academicBackgroundRepo, curriculum, userId));
        creationList.add(addChildFutureFunction(curriculum.getProjects(), projectRepo, curriculum, userId));
        creationList.add(addChildFutureFunction(curriculum.getExperiences(), curriculumExperienceRepo, curriculum, userId));
    }

    public CompletableFuture<CollectionPage<Curriculum>> getCurricula(long accountId, int page, int pageSize) {
        return userAccountService.getUser(accountId)
                .thenCompose(__ -> ServiceUtils.getCollectionPageFuture(curriculumRepo, page, pageSize, new Pair<>("accountId", accountId)));
    }

    public CompletableFuture<Curriculum> getCurriculum(long userId, long cid, String... email) {
        return userAccountService.getUser(userId, email)
                .thenCompose(UserAccount::getCurricula)
                .thenApply(curricula -> {
                    Optional<Curriculum> optionalCurriculum = curricula.stream().filter(curr -> curr.getIdentityKey() == cid).findFirst();
                    if (curricula.isEmpty() || !optionalCurriculum.isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM);
                    return optionalCurriculum.get();
                });
    }

    public Mono<Void> updateCurriculum(Curriculum curriculum, String email) {
        return Mono.fromFuture(
                userAccountService.getUser(curriculum.getAccountId(), email)
                        .thenCompose(userAccount -> getCurriculum(curriculum.getAccountId(), curriculum.getIdentityKey(), email))
                        .thenCompose(curriculum1 -> curriculumRepo.update(curriculum))
        );
    }

    public Mono<Void> deleteCurriculum(long userId, long cid, String name) {
        return Mono.fromFuture(
                getCurriculum(userId, cid, name)
                        .thenCompose(curriculumRepo::delete)
        );
    }

    public <T extends CurriculumChild & DomainObject<Long>> CompletableFuture<T> getCurriculumChild(
            DataRepository<T, Long> repo,
            long accountId,
            long curriculumId,
            long jexpId
    ) {
        return repo.findById(jexpId)
                .thenApply(oChild -> oChild.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND)))
                .thenApply(child -> {
                    if (child.getCurriculumId() != curriculumId || child.getAccountId() != accountId)
                        throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                    return child;
                });
    }
}
