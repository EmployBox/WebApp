package isel.ps.employbox.controllers.curricula;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.PreviousJobsBinder;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutPreviousJobs;
import isel.ps.employbox.services.curricula.CurriculumService;
import isel.ps.employbox.services.curricula.PreviousJobService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula/{cid}/previousJobs")
public class PreviousJobsController {

    private final PreviousJobsBinder previousJobsBinder;
    private final CurriculumService curriculumService;
    private final DataRepository<PreviousJobs, Long> prevJobRepo;
    private final PreviousJobService previousJobService;

    public PreviousJobsController(PreviousJobsBinder previousJobsBinder, CurriculumService curriculumService, DataRepository<PreviousJobs, Long> prevJobRepo, PreviousJobService previousJobService) {
        this.previousJobsBinder = previousJobsBinder;
        this.curriculumService = curriculumService;
        this.prevJobRepo = prevJobRepo;
        this.previousJobService = previousJobService;
    }

    @GetMapping("/{prvJbId}")
    public Mono<OutPreviousJobs> getPreviousJob (
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long prvJbId){
        return previousJobsBinder.bindOutput(curriculumService.getCurriculumChild(prevJobRepo, id, cid, prvJbId));
    }

    @GetMapping
    public Mono<HalCollectionPage> getPreviousJobs(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int numberOfItems
    ){
        return previousJobsBinder.bindOutput(
                previousJobService.getCurriculumPreviousJobs( cid, page, numberOfItems ),
                this.getClass(),
                id,
                cid );
    }

    @PostMapping
    public Mono<PreviousJobs> addPreviousJob (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody PreviousJobs previousJobs,
            Authentication authentication)
    {
        return Mono.fromFuture( previousJobService.addPreviousJobToCurriculum(id,cid, previousJobs,authentication.getName()));
    }

    @PutMapping("/{pvjId}")
    public Mono<Void> updatePreviousJob(
            @PathVariable long pvjId,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody PreviousJobs previousJobs,
            Authentication authentication)
    {
        if(previousJobs.getAccountId() != id || previousJobs.getCurriculumId() != cid)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return previousJobService.updatePreviousJob(pvjId, id, cid, previousJobs,authentication.getName() );
    }

    @DeleteMapping("/{pvjId}")
    public Mono<Void> deletePreviousJob(
            @PathVariable long pvjId,
            @PathVariable long id,
            @PathVariable long cid,
            Authentication authentication)
    {
        return previousJobService.deletePreviousJob(pvjId, id, cid, authentication.getName());
    }
}
