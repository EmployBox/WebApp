package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.services.ServiceUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<CollectionPage<AcademicBackground>> getCurriculumAcademicBackgrounds(long curriculumId, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return curriculumRepo.findById(unitOfWork, curriculumId)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(optional -> optional.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(curriculum -> ServiceUtils.getCollectionPageFuture(academicBackgroundRepo, page, pageSize, new Pair<>("curriculumId", curriculumId)));
    }

    public CompletableFuture<AcademicBackground> addAcademicBackgroundToCurriculum (
            long accountId,
            long curriculumId,
            AcademicBackground academicBackground,
            String email
    ) {
        if(academicBackground.getAccountId() != accountId || academicBackground.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unitOfWork = new UnitOfWork();
        return curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> academicBackgroundRepo.create(unitOfWork, academicBackground))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> academicBackground));
    }

    public Mono<Void> updateAcademicBackground(
            long accountId,
            long curriculumId,
            AcademicBackground academicBackground,
            String email
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return Mono.fromFuture(
                curriculumService.getCurriculum(accountId, curriculumId, email)
                        .thenCompose(curriculum -> academicBackgroundRepo.update(unitOfWork, academicBackground))
                        .thenCompose(res -> unitOfWork.commit())
        );
    }

    public Mono<Void> deleteAcademicBackground(
            long academicBackgroundId,
            long accountId,
            long curriculumId,
            String email
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> academicBackgroundRepo.deleteById(unitOfWork, academicBackgroundId))
                .thenCompose(res -> unitOfWork.commit())
        );
    }
}
