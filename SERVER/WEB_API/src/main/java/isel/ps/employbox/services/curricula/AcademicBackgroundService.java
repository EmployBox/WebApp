package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.MapperRegistry;
import com.github.jayield.rapper.mapper.conditions.EqualCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
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

import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class AcademicBackgroundService {

    private final CurriculumService curriculumService;

    public AcademicBackgroundService(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    public CompletableFuture<CollectionPage<AcademicBackground>> getCurriculumAcademicBackgrounds(long curriculumId, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Curriculum, Long> curriculumMapper = MapperRegistry.getMapper(Curriculum.class, unitOfWork);

        CompletableFuture<CollectionPage<AcademicBackground>> future = curriculumMapper.findById(curriculumId)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(optional -> optional.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(curriculum -> ServiceUtils.getCollectionPageFuture(AcademicBackground.class, page, pageSize, new EqualCondition<>("curriculumId", curriculumId)));
        return handleExceptions(future, unitOfWork);
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
        DataMapper<AcademicBackground, Long> academicBackgroundMapper = MapperRegistry.getMapper(AcademicBackground.class, unitOfWork);

        CompletableFuture<AcademicBackground> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> academicBackgroundMapper.create(academicBackground))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> academicBackground));
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateAcademicBackground(
            long accountId,
            long curriculumId,
            AcademicBackground academicBackground,
            String email
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<AcademicBackground, Long> academicBackgroundMapper = MapperRegistry.getMapper(AcademicBackground.class, unitOfWork);

        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> academicBackgroundMapper.update( academicBackground))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteAcademicBackground(
            long academicBackgroundId,
            long accountId,
            long curriculumId,
            String email
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<AcademicBackground, Long> academicBackgroundMapper = MapperRegistry.getMapper(AcademicBackground.class, unitOfWork);

        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> academicBackgroundMapper.deleteById(academicBackgroundId))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(handleExceptions(future, unitOfWork));
    }
}
