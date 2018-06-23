package isel.ps.employbox.controllers.curricula;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.*;
import isel.ps.employbox.model.entities.*;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.model.input.curricula.childs.InCurriculum;
import isel.ps.employbox.model.output.*;
import isel.ps.employbox.services.curricula.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula")
public class CurriculumController {
    private final DataRepository <CurriculumExperience, Long> currExpRepo;
    private final CurriculumBinder curriculumBinder;
    private final CurriculumExperienceBinder curriculumExperienceBinder;
    private final CurriculumService curriculumService;
    private final CurriculumExperienceService curriculumExperienceService;

    public CurriculumController(
            DataRepository<CurriculumExperience, Long> curriculumExperienceLongDataRepository,
            CurriculumService userService,
            CurriculumBinder curriculumBinder,
            CurriculumExperienceBinder curriculumExperienceBinder,
            CurriculumExperienceService curriculumExperienceService
    ) {
        this.currExpRepo = curriculumExperienceLongDataRepository;
        this.curriculumService = userService;
        this.curriculumBinder = curriculumBinder;
        this.curriculumExperienceBinder = curriculumExperienceBinder;
        this.curriculumExperienceService = curriculumExperienceService;
    }

    @GetMapping
    public Mono<HalCollectionPage> getCurricula(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ){
        return curriculumBinder.bindOutput(
                curriculumService.getCurricula(id, page, pageSize),
                this.getClass(),
                id
        );
    }

    @GetMapping("/{cid}")
    public Mono<OutCurriculum> getCurriculum(@PathVariable long id, @PathVariable long cid){
        return curriculumBinder.bindOutput(
                curriculumService.getCurriculum(id, cid));
    }

    @PostMapping
    public Mono<OutCurriculum> createCurriculum( @PathVariable long id, @RequestBody InCurriculum inCurriculum, Authentication authentication){
        return curriculumBinder.bindOutput(curriculumService.createCurriculum(id, curriculumBinder.bindInput(inCurriculum), authentication.getName()));
    }

    @PutMapping("/{cid}")
    public Mono<Void> updateCurriculum(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InCurriculum inCurriculum,
            Authentication authentication
    ){
        if(inCurriculum.getAccountId() != id || inCurriculum.getCurriculumId() != cid) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Curriculum curriculum = curriculumBinder.bindInput(inCurriculum);
        return curriculumService.updateCurriculum(curriculum,authentication.getName() );
    }

    @DeleteMapping("/{cid}")
    public Mono<Void> deleteCurriculum( @PathVariable long id, @PathVariable long cid, Authentication authentication){
        return curriculumService.deleteCurriculum(id, cid, authentication.getName());
    }
}
