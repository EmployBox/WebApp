package isel.ps.employbox.services.curriculumServices;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.model.entities.CurriculumChilds.AcademicBackground;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class AcademicBackgroundService {

    public final CurriculumService curriculumService;
    public final DataRepository<AcademicBackground, Long> academicBackgroundRepo;

    public AcademicBackgroundService(CurriculumService curriculumService, DataRepository<AcademicBackground, Long> academicBackgroundRepo) {
        this.curriculumService = curriculumService;
        this.academicBackgroundRepo = academicBackgroundRepo;
    }

    public CompletableFuture<Stream<AcademicBackground>> getCurriculumAcademicBackgrounds(long curriculumId){
        return academicBackgroundRepo.findWhere(new Pair<>("curriculumId",curriculumId))
                .thenApply( academicBackgrounds -> academicBackgrounds.stream());
    }

    public CompletableFuture<AcademicBackground> addAcademicBackgroundToCurriculum (
            long academicBackgroundId,
            long accountId,
            long curriculumId,
            AcademicBackground academicBackground,
            String email
    ) {
        if(academicBackground.getAccountId() != accountId || academicBackground.getCurriculumId() != curriculumId || academicBackgroundId != academicBackground.getAccountId())
            throw new ConflictException(ErrorMessages.badRequest_IdsMismatch);

        return curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose( __ -> academicBackgroundRepo.create( academicBackground))
                .thenApply( res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                    return academicBackground;
                });
    }

    public Mono<Void> updateAcademicBackground(
            long accountId,
            long curriculumId,
            AcademicBackground academicBackground,
            String email
    ) {
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(__ -> academicBackgroundRepo.update(academicBackground)
                        .thenAccept(
                                res -> {
                                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                                }
                        ))
        );
    }

    public Mono<Void> deleteAcademicBackground(
            long academicBackgroundId,
            long accountId,
            long curriculumId,
            String email
    ) {
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(__ -> academicBackgroundRepo.deleteById(academicBackgroundId))
                .thenAccept(
                        res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                        }
                )
        );
    }
}
