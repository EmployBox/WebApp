package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.services.ServiceUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class PreviousJobService {
    private final DataRepository<PreviousJobs, Long> previousJobsRepo;
    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final CurriculumService curriculumService;

    public PreviousJobService(DataRepository<PreviousJobs, Long> previousJobsRepo, DataRepository<Curriculum, Long> curriculumRepo, CurriculumService curriculumService) {
        this.previousJobsRepo = previousJobsRepo;
        this.curriculumRepo = curriculumRepo;
        this.curriculumService = curriculumService;
    }

    public CompletableFuture<CollectionPage<PreviousJobs>> getCurriculumPreviousJobs(long curriculumId, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<CollectionPage<PreviousJobs>> future = curriculumRepo.findById(unitOfWork, curriculumId)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(ocurriculum -> ocurriculum.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(__ -> ServiceUtils.getCollectionPageFuture(previousJobsRepo, page, pageSize, new Pair<>("curriculumId", curriculumId)));
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<PreviousJobs> addPreviousJobToCurriculum (
            long accountId,
            long curriculumId,
            PreviousJobs previousJobs,
            String email
    ) {
        if(previousJobs.getAccountId() != accountId || previousJobs.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<PreviousJobs> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> previousJobsRepo.create(unitOfWork, previousJobs))
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
        if(previousJobs.getAccountId() != pvjId)
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unitOfWork = new UnitOfWork();
        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> previousJobsRepo.update(unitOfWork, previousJobs))
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
        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> previousJobsRepo.deleteById(unitOfWork, previousJobId))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

}
