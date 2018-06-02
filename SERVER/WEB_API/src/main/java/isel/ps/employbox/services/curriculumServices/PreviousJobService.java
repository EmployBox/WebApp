package isel.ps.employbox.services.curriculumServices;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.CurriculumChilds.PreviousJobs;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

    public CompletableFuture<CollectionPage<PreviousJobs>> getCurriculumPreviousJobs(long curriculumId, int page){
        return curriculumRepo.findById(curriculumId)
                .thenApply( ocurriculum -> ocurriculum.orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(Curriculum::getPreviousJobs)
                .thenApply( list -> new CollectionPage<>(
                        list.size(),
                        page,
                        list.stream()
                                .skip(CollectionPage.PAGE_SIZE * page)
                                .limit(CollectionPage.PAGE_SIZE))
                );
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
                .thenCompose( __ ->previousJobsRepo.create( previousJobs))
                .thenApply( res -> {
                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                    return previousJobs;
                });
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
                .thenCompose(__ -> previousJobsRepo.update(previousJobs))
                .thenAccept(res -> {
                            if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                        })
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
                        .thenCompose(__ -> previousJobsRepo.deleteById(previousJobId))
                        .thenAccept(res -> {
                                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                                })
        );
    }

}
