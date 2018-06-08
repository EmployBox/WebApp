package isel.ps.employbox.controllers.curricula;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.AcademicBackgroundBinder;
import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.model.input.InAcademicBackground;
import isel.ps.employbox.model.output.HalCollectionPage;
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
    private final DataRepository<AcademicBackground, Long> backgroundRepo;

    public AcademicBackgroundController(
            AcademicBackgroundBinder academicBackgroundBinder,
            AcademicBackgroundService academicBackgroundService,
            CurriculumService curriculumService,
            DataRepository<AcademicBackground, Long> backgroundRepo
    ) {
        this.academicBackgroundBinder = academicBackgroundBinder;
        this.academicBackgroundService = academicBackgroundService;
        this.curriculumService = curriculumService;
        this.backgroundRepo = backgroundRepo;
    }

    @GetMapping
    public Mono<HalCollectionPage> getAllAcademicBackground(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int numberOfItems
    ) {
        return academicBackgroundBinder.bindOutput(
                academicBackgroundService.getCurriculumAcademicBackgrounds(cid, page, numberOfItems),
                this.getClass(),
                id,
                cid
        );
    }

    @GetMapping("/{academicId}")
    public Mono<OutAcademicBackground> getAcademicBackground(
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long academicId
    ){
        return academicBackgroundBinder.bindOutput(curriculumService.getCurriculumChild(backgroundRepo, id, cid, academicId));
    }

    @PostMapping
    public Mono<OutAcademicBackground> addAcademicBackground (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InAcademicBackground inAcademicBackground,
            Authentication authentication
    ) {
        AcademicBackground academicBackground = academicBackgroundBinder.bindInput(inAcademicBackground);
        CompletableFuture<AcademicBackground> future = academicBackgroundService.addAcademicBackgroundToCurriculum(id, cid, academicBackground, authentication.getName());
        return academicBackgroundBinder.bindOutput(future);
    }

    @PutMapping("/{abkId}")
    public Mono<Void> updateAcademicBackground(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody AcademicBackground academicBackground,
            Authentication authentication
    ) {
        if(academicBackground.getAccountId() != id || academicBackground.getCurriculumId() != cid)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return academicBackgroundService.updateAcademicBackground(id, cid, academicBackground, authentication.getName() );
    }

    @DeleteMapping("/{abkId}")
    public Mono<Void> deleteAcademicBackground(
            @PathVariable long abkId,
            @PathVariable long id,
            @PathVariable long cid,
            Authentication authentication)
    {
        return academicBackgroundService.deleteAcademicBackground(abkId, id, cid, authentication.getName());
    }
}
