package isel.ps.employbox.controllers.curricula;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.CurriculumExperienceBinder;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.model.input.curricula.childs.InCurriculumExperience;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutCurriculumExperience;
import isel.ps.employbox.services.curricula.CurriculumExperienceService;
import isel.ps.employbox.services.curricula.CurriculumService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula/{cid}/experiences")
public class CurriculumExperienceController {

    private final CurriculumExperienceBinder curriculumExperienceBinder;
    private final CurriculumService curriculumService;
    private final CurriculumExperienceService curriculumExperienceService;

    public CurriculumExperienceController(CurriculumExperienceBinder curriculumExperienceBinder, CurriculumService curriculumService, CurriculumExperienceService curriculumExperienceService) {
        this.curriculumExperienceBinder = curriculumExperienceBinder;
        this.curriculumService = curriculumService;
        this.curriculumExperienceService = curriculumExperienceService;
    }

    @GetMapping("/{expId}")
    public Mono<OutCurriculumExperience> getCurriculumExperience (
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long expId){
        CompletableFuture<OutCurriculumExperience> future = curriculumService.getCurriculumChild(CurriculumExperience.class, id, cid, expId)
                .thenCompose(curriculumExperienceBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @GetMapping
    public Mono<HalCollectionPage<CurriculumExperience>> getCurriculumExperiences (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        CompletableFuture<HalCollectionPage<CurriculumExperience>> future = curriculumExperienceService.getCurriculumExperiences(cid, page, pageSize)
                .thenCompose(curriculumExperienceCollectionPage -> curriculumExperienceBinder.bindOutput(curriculumExperienceCollectionPage, this.getClass(), id, cid));
        return Mono.fromFuture(future);
    }

    @PostMapping
    public Mono<CurriculumExperience> addCurriculumExperienceToJob(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InCurriculumExperience inCurriculumExperience,
            Authentication authentication)
    {
        CurriculumExperience curriculumExperience = curriculumExperienceBinder.bindInput(inCurriculumExperience);
        return Mono.fromFuture( curriculumExperienceService.addCurriculumExperience(id,cid, curriculumExperience,authentication.getName()));
    }

    @PutMapping("/{ceId}")
    public Mono<Void> updateCurriculumExperience(
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long ceId,
            @RequestBody InCurriculumExperience incurriculumExperience,
            Authentication authentication
    ){
        if( incurriculumExperience.getCurriculumId() != cid || incurriculumExperience.getCurriculumExperienceId() != ceId)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        CurriculumExperience curriculumExperience = curriculumExperienceBinder.bindInput(incurriculumExperience);
        return curriculumExperienceService.updateCurriculumExperience(id, cid, curriculumExperience,authentication.getName() );
    }

    @DeleteMapping("/{ceId}")
    public Mono<Void> deleteCurriculumExperience(
            @PathVariable long ceId,
            @PathVariable long id,
            @PathVariable long cid,
            Authentication authentication)
    {
        return curriculumExperienceService.deleteCurriculumExperience(ceId, id, cid, authentication.getName());
    }
}
