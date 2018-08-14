package isel.ps.employbox.controllers.curricula;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.PreviousJobsBinder;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.model.input.curricula.childs.InPreviousJobs;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutPreviousJobs;
import isel.ps.employbox.services.curricula.CurriculumService;
import isel.ps.employbox.services.curricula.PreviousJobService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula/{cid}/previousJobs")
public class PreviousJobsController {

    private final PreviousJobsBinder previousJobsBinder;
    private final CurriculumService curriculumService;
    private final PreviousJobService previousJobService;

    public PreviousJobsController(PreviousJobsBinder previousJobsBinder, CurriculumService curriculumService,PreviousJobService previousJobService) {
        this.previousJobsBinder = previousJobsBinder;
        this.curriculumService = curriculumService;
        this.previousJobService = previousJobService;
    }

    @GetMapping("/{prvJbId}")
    public Mono<OutPreviousJobs> getPreviousJob (
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long prvJbId
    ){
        CompletableFuture<OutPreviousJobs> future = curriculumService.getCurriculumChild(PreviousJobs.class, id, cid, prvJbId)
                .thenCompose(previousJobsBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @GetMapping
    public Mono<HalCollectionPage<PreviousJobs>> getPreviousJobs(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        CompletableFuture<HalCollectionPage<PreviousJobs>> future = previousJobService.getCurriculumPreviousJobs(id, cid, page, pageSize)
                .thenCompose(previousJobsCollectionPage -> previousJobsBinder.bindOutput(previousJobsCollectionPage, this.getClass(), id, cid));
        return Mono.fromFuture(future);
    }

    @PostMapping
    public Mono<PreviousJobs> addPreviousJob (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InPreviousJobs inPreviousJobs,
            Authentication authentication)
    {
        PreviousJobs previousJobs = previousJobsBinder.bindInput(inPreviousJobs);
        return Mono.fromFuture( previousJobService.addPreviousJobToCurriculum(id,cid, previousJobs,authentication.getName()));
    }

    @PutMapping("/{pvjId}")
    public Mono<Void> updatePreviousJob(
            @PathVariable long pvjId,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InPreviousJobs inPreviousJobs,
            Authentication authentication)
    {
        PreviousJobs previousJobs = previousJobsBinder.bindInput(inPreviousJobs);
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
