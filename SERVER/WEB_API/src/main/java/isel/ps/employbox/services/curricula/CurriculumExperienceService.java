package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.model.entities.CurriculumChilds.CurriculumExperience;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class CurriculumExperienceService {
    private final DataRepository<CurriculumExperience, Long> curriculumExperienceRepo;
    private final CurriculumService curriculumService;


    public CurriculumExperienceService(DataRepository<CurriculumExperience, Long> curriculumExperienceRepo, CurriculumService curriculumService) {
        this.curriculumExperienceRepo = curriculumExperienceRepo;
        this.curriculumService = curriculumService;
    }

    public CompletableFuture<Stream<CurriculumExperience>> getCurriculumExperiences(long curriculumId){
        return curriculumExperienceRepo.findWhere(new Pair<>("curriculumId",curriculumId))
                .thenApply(Collection::stream);
    }

    public CompletableFuture<CurriculumExperience> addCurriculumExperience(
            long accountId,
            long curriculumId,
            CurriculumExperience curriculumExperience,
            String email
    ) {
        if (curriculumExperience.getAccountId() != accountId || curriculumExperience.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        return curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> curriculumExperienceRepo.create(curriculumExperience))
                .thenApply(res -> curriculumExperience);
    }

    public Mono<Void> updateCurriculumExperience(
            long accountId,
            long curriculumId,
            CurriculumExperience curriculumExperience,
            String email
    ) {
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> curriculumExperienceRepo.update(curriculumExperience)
                )
        );
    }

    public Mono<Void> deleteCurriculumExperience(
            long curriculumExperienceId,
            long accountId,
            long curriculumId,
            String email
    ) {
        return Mono.fromFuture(
                curriculumService.getCurriculum(accountId, curriculumId, email)
                        .thenCompose(curriculum -> curriculumExperienceRepo.deleteById(curriculumExperienceId))
        );
    }
}
