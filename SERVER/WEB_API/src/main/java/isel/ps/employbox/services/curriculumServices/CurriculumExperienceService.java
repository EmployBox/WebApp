package isel.ps.employbox.services.curriculumServices;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.CurriculumChilds.CurriculumExperience;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class CurriculumExperienceService {
    private final DataRepository<CurriculumExperience, Long> curriculumExperienceRepo;
    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final CurriculumService curriculumService;


    public CurriculumExperienceService(DataRepository<CurriculumExperience, Long> curriculumExperienceRepo, DataRepository<Curriculum, Long> curriculumRepo, CurriculumService curriculumService) {
        this.curriculumExperienceRepo = curriculumExperienceRepo;
        this.curriculumRepo = curriculumRepo;
        this.curriculumService = curriculumService;
    }

    public CompletableFuture<CollectionPage<CurriculumExperience>> getCurriculumExperiences(long curriculumId, int page){
        return curriculumRepo.findById(curriculumId)
                .thenApply( ocurriculum -> ocurriculum.orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(Curriculum::getExperiences)
                .thenApply( list -> new CollectionPage<>(
                        list.size(),
                        page,
                        list.stream()
                                .skip(CollectionPage.PAGE_SIZE * page)
                                .limit(CollectionPage.PAGE_SIZE))
                );
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
                .thenCompose(__ -> curriculumExperienceRepo.create(curriculumExperience))
                .thenApply(res -> {
                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                    return curriculumExperience;
                });
    }

    public Mono<Void> updateCurriculumExperience(
            long accountId,
            long curriculumId,
            CurriculumExperience curriculumExperience,
            String email
    ) {
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(__ -> curriculumExperienceRepo.update(curriculumExperience)
                        .thenAccept(
                                res -> {
                                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                                }
                        ))
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
                        .thenCompose(__ -> curriculumExperienceRepo.deleteById(curriculumExperienceId))
                        .thenAccept(
                                res -> {
                                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                                }
                        )
        );
    }
}
