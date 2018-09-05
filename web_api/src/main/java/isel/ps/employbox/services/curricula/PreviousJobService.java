package isel.ps.employbox.services.curricula;


import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.MapperRegistry;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.services.ServiceUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class PreviousJobService {
    private final CurriculumService curriculumService;

    public PreviousJobService(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    public CompletableFuture<CollectionPage<PreviousJobs>> getCurriculumPreviousJobs(long userId, long curriculumId, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();

        CompletableFuture<CollectionPage<PreviousJobs>> future = curriculumService.getCurriculum(userId, curriculumId)
                .thenCompose(__ -> ServiceUtils.getCollectionPageFuture(PreviousJobs.class, page, pageSize, new EqualAndCondition<>("curriculumId", curriculumId)));
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<PreviousJobs> addPreviousJobToCurriculum (
            long accountId,
            long curriculumId,
            PreviousJobs previousJobs,
            String email
    ) {
        if(previousJobs.getAccountId() != accountId || previousJobs.getCurriculumId() != curriculumId)
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<PreviousJobs, Long> previousJobsMapper = MapperRegistry.getMapper(PreviousJobs.class, unitOfWork);
        CompletableFuture<PreviousJobs> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> previousJobsMapper.create(previousJobs))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> previousJobs));
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updatePreviousJob(
            long pvjId,
            long accountId,
            long curriculumId,
            PreviousJobs previousJobs,
            String email
    ) {
        if(previousJobs.getIdentityKey() != pvjId)
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<PreviousJobs, Long> previousJobsMapper = MapperRegistry.getMapper(PreviousJobs.class, unitOfWork);
        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> previousJobsMapper.update(previousJobs))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(handleExceptions(future, unitOfWork));
    }

    public Mono<Void> deletePreviousJob(
            long previousJobId,
            long accountId,
            long curriculumId,
            String email
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<PreviousJobs, Long> previousJobsMapper = MapperRegistry.getMapper(PreviousJobs.class, unitOfWork);
        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> previousJobsMapper.deleteById(previousJobId))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

}
