package isel.ps.employbox.services.curricula;


import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.MapperRegistry;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.services.ServiceUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class CurriculumExperienceService {
    private final CurriculumService curriculumService;


    public CurriculumExperienceService(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    public CompletableFuture<CollectionPage<CurriculumExperience>> getCurriculumExperiences(long curriculumId, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Curriculum, Long> curriculumMapper = MapperRegistry.getMapper(Curriculum.class, unitOfWork);

        CompletableFuture<CollectionPage<CurriculumExperience>> future = curriculumMapper.findById(curriculumId)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(ocurriculum -> ocurriculum.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(__ -> ServiceUtils.getCollectionPageFuture(CurriculumExperience.class, page, pageSize, new EqualAndCondition<>("curriculumId", curriculumId)));
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<CurriculumExperience> addCurriculumExperience(
            long accountId,
            long curriculumId,
            CurriculumExperience curriculumExperience,
            String email
    ) {
        if (curriculumExperience.getAccountId() != accountId || curriculumExperience.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<CurriculumExperience, Long> curriculumExperienceMapper = MapperRegistry.getMapper(CurriculumExperience.class, unitOfWork);

        CompletableFuture<CurriculumExperience> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> curriculumExperienceMapper.create(curriculumExperience)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> curriculumExperience)));
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateCurriculumExperience(
            long accountId,
            long curriculumId,
            CurriculumExperience curriculumExperience,
            String email
    ) {
        if(curriculumExperience.getAccountId() != accountId)
            throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<CurriculumExperience, Long> curriculumExperienceMapper = MapperRegistry.getMapper(CurriculumExperience.class, unitOfWork);
        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> curriculumExperienceMapper.update(curriculumExperience))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(handleExceptions(future, unitOfWork));
    }

    public Mono<Void> deleteCurriculumExperience(
            long curriculumExperienceId,
            long accountId,
            long curriculumId,
            String email
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<CurriculumExperience, Long> curriculumExperienceMapper = MapperRegistry.getMapper(CurriculumExperience.class, unitOfWork);
        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> curriculumExperienceMapper.deleteById(curriculumExperienceId))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
