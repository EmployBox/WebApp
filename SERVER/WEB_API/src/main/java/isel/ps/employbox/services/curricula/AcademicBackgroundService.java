package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.CurriculumChilds.AcademicBackground;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AcademicBackgroundService {

    private final CurriculumService curriculumService;
    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final DataRepository<AcademicBackground, Long> academicBackgroundRepo;

    public AcademicBackgroundService(CurriculumService curriculumService, DataRepository<Curriculum, Long> curriculumRepo, DataRepository<AcademicBackground, Long> academicBackgroundRepo) {
        this.curriculumService = curriculumService;
        this.curriculumRepo = curriculumRepo;
        this.academicBackgroundRepo = academicBackgroundRepo;
    }

    public CompletableFuture<CollectionPage<AcademicBackground>> getCurriculumAcademicBackgrounds(long curriculumId, int page) {
        return curriculumRepo.findById(curriculumId)
                .thenApply(ocurriculum -> ocurriculum.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(curriculum -> curriculum.getAcademicBackground())
                .thenApply(list -> new CollectionPage(
                        list.size(),
                        page,
                        list.stream()
                                .skip(CollectionPage.PAGE_SIZE * page)
                                .limit(CollectionPage.PAGE_SIZE)
                                .collect(Collectors.toList())
                        )
                );
    }

    public CompletableFuture<AcademicBackground> addAcademicBackgroundToCurriculum (
            long academicBackgroundId,
            long accountId,
            long curriculumId,
            AcademicBackground academicBackground,
            String email
    ) {
        if(academicBackground.getAccountId() != accountId || academicBackground.getCurriculumId() != curriculumId || academicBackgroundId != academicBackground.getAccountId())
            throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

        return curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> academicBackgroundRepo.create( academicBackground))
                .thenApply(res -> academicBackground);
    }

    public Mono<Void> updateAcademicBackground(
            long accountId,
            long curriculumId,
            AcademicBackground academicBackground,
            String email
    ) {
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> academicBackgroundRepo.update(academicBackground))
        );
    }

    public Mono<Void> deleteAcademicBackground(
            long academicBackgroundId,
            long accountId,
            long curriculumId,
            String email
    ) {
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> academicBackgroundRepo.deleteById(academicBackgroundId))
        );
    }
}
