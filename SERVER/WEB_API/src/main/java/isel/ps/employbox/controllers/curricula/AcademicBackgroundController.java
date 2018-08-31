package isel.ps.employbox.controllers.curricula;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.AcademicBackgroundBinder;
import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.model.input.curricula.childs.InAcademicBackground;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutAcademicBackground;
import isel.ps.employbox.services.curricula.AcademicBackgroundService;
import isel.ps.employbox.services.curricula.CurriculumService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula/{cid}/academic")
public class AcademicBackgroundController {

    private final AcademicBackgroundBinder academicBackgroundBinder;
    private final AcademicBackgroundService academicBackgroundService;
    private final CurriculumService curriculumService;

    public AcademicBackgroundController(
            AcademicBackgroundBinder academicBackgroundBinder,
            AcademicBackgroundService academicBackgroundService,
            CurriculumService curriculumService
    ) {
        this.academicBackgroundBinder = academicBackgroundBinder;
        this.academicBackgroundService = academicBackgroundService;
        this.curriculumService = curriculumService;
    }

    @GetMapping
    public Mono<HalCollectionPage<AcademicBackground>> getAllAcademicBackground(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        CompletableFuture<HalCollectionPage<AcademicBackground>> future = academicBackgroundService.getCurriculumAcademicBackgrounds(cid, page, pageSize)
                .thenCompose(academicBackgroundCollectionPage -> academicBackgroundBinder.bindOutput(academicBackgroundCollectionPage, this.getClass(), id, cid));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{academicId}")
    public Mono<OutAcademicBackground> getAcademicBackground(
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long academicId
    ){
        CompletableFuture<OutAcademicBackground> future = curriculumService.getCurriculumChild(AcademicBackground.class, id, cid, academicId)
                .thenCompose(academicBackgroundBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @PostMapping
    public Mono<OutAcademicBackground> addAcademicBackground (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InAcademicBackground inAcademicBackground,
            Authentication authentication
    ) {
        AcademicBackground academicBackground = academicBackgroundBinder.bindInput(inAcademicBackground);
        CompletableFuture<OutAcademicBackground> future = academicBackgroundService.addAcademicBackgroundToCurriculum(id, cid, academicBackground, authentication.getName())
                .thenCompose(academicBackgroundBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @PutMapping("/{abkId}")
    public Mono<Void> updateAcademicBackground(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InAcademicBackground inAcademicBackground,
            Authentication authentication
    ) {
        if(inAcademicBackground.getAccountId() != id || inAcademicBackground.getCurriculumId() != cid)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        AcademicBackground academicBackground = academicBackgroundBinder.bindInput(inAcademicBackground);
        return academicBackgroundService.updateAcademicBackground(id, cid, academicBackground, authentication.getName() );
    }

    @DeleteMapping("/{abkId}")
    public Mono<Void> deleteAcademicBackground(
            @PathVariable long abkId,
            @PathVariable long id,
            @PathVariable long cid,
            Authentication authentication
    ) {
        return academicBackgroundService.deleteAcademicBackground(abkId, id, cid, authentication.getName());
    }
}
