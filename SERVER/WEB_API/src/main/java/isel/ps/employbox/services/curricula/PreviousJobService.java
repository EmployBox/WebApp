package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.Transaction;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.CurriculumChilds.PreviousJobs;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<CollectionPage<PreviousJobs>> getCurriculumPreviousJobs(long curriculumId, int pageSize, int page) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];

        return curriculumRepo.findById(curriculumId)
                .thenApply(ocurriculum -> ocurriculum.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(__ -> new Transaction(Connection.TRANSACTION_SERIALIZABLE)
                        .andDo(() -> previousJobsRepo.findWhere(page, pageSize, new Pair<>("curriculumId", curriculumId))
                                .thenCompose(listRes -> {
                                    list[0] = listRes;
                                    return previousJobsRepo.getNumberOfEntries(new Pair<>("curriculumId", curriculumId));
                                })
                                .thenApply(collectionSize -> ret[0] = new CollectionPage(
                                        collectionSize,
                                        pageSize,
                                        page,
                                        list[0])
                                )
                        ).commit())
                .thenApply(__ -> ret[0]);
    }


    public CompletableFuture<PreviousJobs> addPreviousJobToCurriculum (
            long accountId,
            long curriculumId,
            PreviousJobs previousJobs,
            String email
    ) {
        if(previousJobs.getAccountId() != accountId || previousJobs.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        return curriculumService.getCurriculum(accountId, curriculumId,email)
                .thenCompose(curriculum -> previousJobsRepo.create( previousJobs))
                .thenApply(aVoid -> previousJobs);
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
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> previousJobsRepo.update(previousJobs))
        );
    }

    public Mono<Void> deletePreviousJob(
            long previousJobId,
            long accountId,
            long curriculumId,
            String email
    ) {
        return Mono.fromFuture(
                curriculumService.getCurriculum(accountId, curriculumId, email)
                        .thenCompose(curriculum -> previousJobsRepo.deleteById(previousJobId))
        );
    }

}
