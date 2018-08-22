package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import io.vertx.ext.sql.TransactionIsolation;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.curricula.childs.*;
import isel.ps.employbox.services.ServiceUtils;
import isel.ps.employbox.services.UserAccountService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class CurriculumService {
    private final UserAccountService userAccountService;

    public CurriculumService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    public CompletableFuture<Curriculum> createCurriculum(long userId, Curriculum curriculum, String email) {
        if (curriculum.getAccountId() != userId)
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

        UnitOfWork unitOfWork = new UnitOfWork(TransactionIsolation.SERIALIZABLE);
        DataMapper<Curriculum, Long> curriculumMapper = getMapper(Curriculum.class, unitOfWork);

        CompletableFuture<Curriculum> future = userAccountService.getUser(userId, email)
                .thenCompose(userAccount -> curriculumMapper.create(curriculum)
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
            Class<T> tClass,
            Curriculum curriculum,
            long userId
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<T, K> mapper = getMapper(tClass, unitOfWork);

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
                    return mapper.createAll(list);
                })
                .thenCompose(aVoid -> unitOfWork.commit());

        return handleExceptions(future1, unitOfWork);
    }

    private void populateChildList(List<CompletableFuture<Void>> creationList, Curriculum curriculum, long userId) {
        creationList.add(addChildFutureFunction(curriculum.getPreviousJobs(), PreviousJobs.class, curriculum, userId));
        creationList.add(addChildFutureFunction(curriculum.getAcademicBackground(), AcademicBackground.class, curriculum, userId));
        creationList.add(addChildFutureFunction(curriculum.getProjects(), Project.class, curriculum, userId));
        creationList.add(addChildFutureFunction(curriculum.getExperiences(), CurriculumExperience.class, curriculum, userId));
    }

    public CompletableFuture<CollectionPage<Curriculum>> getCurricula(long accountId, int page, int pageSize, String orderColumn, String orderClause) {
        ArrayList<Condition> conditions = new ArrayList<>();
        ServiceUtils.evaluateOrderClause(orderColumn,orderClause, conditions);
        conditions.add( new EqualAndCondition<>("accountId", accountId));


        return userAccountService.getUser(accountId)
                .thenCompose(userAccount -> ServiceUtils.getCollectionPageFuture(Curriculum.class, page, pageSize, conditions.toArray(new Condition[conditions.size()])));
    }

    public CompletableFuture<Curriculum> getCurriculum(long userId, long cid, String... email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<Curriculum> future = userAccountService.getUser(userId, email)
                .thenCompose(userAccount -> userAccount.getCurricula().apply(unitOfWork))
                .thenCompose(res -> unitOfWork.commit().thenApply(ignored -> res))
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
        DataMapper<Curriculum, Long> curriculumMapper = getMapper(Curriculum.class, unitOfWork);

        CompletableFuture<Void> future = userAccountService.getUser(curriculum.getAccountId(), email)
                .thenCompose(userAccount -> getCurriculum(curriculum.getAccountId(), curriculum.getIdentityKey(), email))
                .thenCompose(curriculum1 -> curriculumMapper.update(curriculum))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteCurriculum(long userId, long cid, String... email) {
        List<Curriculum> curriculum = new ArrayList<>(1);

        UnitOfWork unitOfWork = new UnitOfWork();

        DataMapper<AcademicBackground, Long> academicBackgroundMapper = getMapper(AcademicBackground.class, unitOfWork);
        DataMapper<Project, Long> projectMapper = getMapper(Project.class, unitOfWork);
        DataMapper<PreviousJobs, Long> previousJobsMapper = getMapper(PreviousJobs.class, unitOfWork);
        DataMapper<CurriculumExperience, Long> curriculumExperienceMapper = getMapper(CurriculumExperience.class, unitOfWork);
        DataMapper<Curriculum, Long> curriculumMapper = getMapper(Curriculum.class, unitOfWork);

        CompletableFuture<Void> future = getCurriculum(userId, cid, email)
                .thenAccept(curriculum1 -> curriculum.add(0, curriculum1))
                .thenCompose(aVoid ->
                        curriculum.get(0).getAcademicBackground().apply(unitOfWork)
                                .thenApply(academicBackgrounds -> academicBackgrounds.stream().map(AcademicBackground::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(academicBackgroundMapper::deleteAll)

                ).thenCompose(aVoid ->
                        curriculum.get(0).getProjects().apply(unitOfWork)
                                .thenApply(projects -> projects.stream().map(Project::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(projectMapper::deleteAll))
                .thenCompose(aVoid ->
                        curriculum.get(0).getPreviousJobs().apply(unitOfWork)
                                .thenApply(previousJobs -> previousJobs.stream().map(PreviousJobs::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(previousJobsMapper::deleteAll)
                )
                .thenCompose(aVoid ->
                        curriculum.get(0).getExperiences().apply(unitOfWork)
                                .thenApply(curriculumExperiences -> curriculumExperiences.stream().map(CurriculumExperience::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(curriculumExperienceMapper::deleteAll)
                )
                .thenCompose(aVoid -> curriculumMapper.deleteById(cid))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public <T extends CurriculumChild & DomainObject<Long>> CompletableFuture<T> getCurriculumChild(
            Class<T> tClass,
            long accountId,
            long curriculumId,
            long jexpId
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<T, Long> mapper = getMapper(tClass, unitOfWork);

        CompletableFuture<T> future = mapper.findById(jexpId)
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
