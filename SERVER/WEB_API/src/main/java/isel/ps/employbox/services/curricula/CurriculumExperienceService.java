package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.services.ServiceUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CurriculumExperienceService {
    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final DataRepository<CurriculumExperience, Long> curriculumExperienceRepo;
    private final CurriculumService curriculumService;


    public CurriculumExperienceService(DataRepository<Curriculum, Long> curriculumRepo, DataRepository<CurriculumExperience, Long> curriculumExperienceRepo, CurriculumService curriculumService) {
        this.curriculumRepo = curriculumRepo;
        this.curriculumExperienceRepo = curriculumExperienceRepo;
        this.curriculumService = curriculumService;
    }

    public CompletableFuture<CollectionPage<CurriculumExperience>> getCurriculumExperiences(long curriculumId, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return curriculumRepo.findById(unitOfWork, curriculumId)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(ocurriculum -> ocurriculum.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(__ -> ServiceUtils.getCollectionPageFuture( curriculumExperienceRepo, page, pageSize, new Pair<>("curriculumId", curriculumId)));
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
        return curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenCompose(curriculum -> curriculumExperienceRepo.create(unitOfWork, curriculumExperience))
                .thenApply(res -> curriculumExperience);
    }

    public Mono<Void> updateCurriculumExperience(
            long accountId,
            long curriculumId,
            CurriculumExperience curriculumExperience,
            String email
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> curriculumExperienceRepo.update(unitOfWork, curriculumExperience))
                .thenCompose(res -> unitOfWork.commit())
        );
    }

    public Mono<Void> deleteCurriculumExperience(
            long curriculumExperienceId,
            long accountId,
            long curriculumId,
            String email
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return Mono.fromFuture(
                curriculumService.getCurriculum(accountId, curriculumId, email)
                        .thenCompose(curriculum -> curriculumExperienceRepo.deleteById(unitOfWork, curriculumExperienceId))
                        .thenCompose(res -> unitOfWork.commit())
        );
    }
}
