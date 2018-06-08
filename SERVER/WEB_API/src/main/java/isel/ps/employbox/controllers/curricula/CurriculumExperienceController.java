package isel.ps.employbox.controllers.curricula;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.CurriculumExperienceBinder;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutCurriculumExperience;
import isel.ps.employbox.services.curricula.CurriculumExperienceService;
import isel.ps.employbox.services.curricula.CurriculumService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula/{cid}/experiences")
public class CurriculumExperienceController {

    private final CurriculumExperienceBinder curriculumExperienceBinder;
    private final CurriculumService curriculumService;
    private final DataRepository<CurriculumExperience, Long> currExpRepo;
    private final CurriculumExperienceService curriculumExperienceService;

    public CurriculumExperienceController(CurriculumExperienceBinder curriculumExperienceBinder, CurriculumService curriculumService, DataRepository<CurriculumExperience, Long> currExpRepo, CurriculumExperienceService curriculumExperienceService) {
        this.curriculumExperienceBinder = curriculumExperienceBinder;
        this.curriculumService = curriculumService;
        this.currExpRepo = currExpRepo;
        this.curriculumExperienceService = curriculumExperienceService;
    }

    @GetMapping("/{expId}")
    public Mono<OutCurriculumExperience> getCurriculumExperience (
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long expId){
        return curriculumExperienceBinder.bindOutput(curriculumService.getCurriculumChild(currExpRepo, id, cid, expId));
    }

    @GetMapping
    public Mono<HalCollectionPage> getCurriculumExperiences (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int numberOfItems
    ){
        return curriculumExperienceBinder.bindOutput(
                curriculumExperienceService.getCurriculumExperiences(cid, page, numberOfItems),
                this.getClass(),
                id
        );
    }

    @PostMapping
    public Mono<CurriculumExperience> addCurriculumExperienceToJob(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody CurriculumExperience curriculumExperience,
            Authentication authentication)
    {
        return Mono.fromFuture( curriculumExperienceService.addCurriculumExperience(id,cid, curriculumExperience,authentication.getName()));
    }

    @PutMapping("/{ceId}")
    public Mono<Void> updateCurriculumExperience(
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long jeId,
            @RequestBody CurriculumExperience curriculumExperience,
            Authentication authentication
    ){
        if(curriculumExperience.getAccountId() != id || curriculumExperience.getCurriculumId() != cid || curriculumExperience.getIdentityKey() != jeId)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
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
