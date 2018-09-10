package isel.ps.employbox.controllers.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.jobs.ApplicationBinder;
import isel.ps.employbox.model.binders.jobs.JobBinder;
import isel.ps.employbox.model.entities.jobs.Job;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.input.InSchedule;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutJob;
import isel.ps.employbox.services.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;
    private final ApplicationBinder applicationBinder;
    private final JobBinder jobBinder;

    public JobController(JobService jobService, ApplicationBinder applicationBinder, JobBinder jobBinder) {
        this.jobService = jobService;
        this.applicationBinder = applicationBinder;
        this.jobBinder = jobBinder;
    }
    @GetMapping
    public Mono<HalCollectionPage<Job>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "") String address,
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false) Integer wage,
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false, defaultValue = "") String offerType,
            @RequestParam(required = false, defaultValue = "0") int ratingLow,
            @RequestParam(required = false, defaultValue = "10") int ratingHigh,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    )  {
        CompletableFuture<HalCollectionPage<Job>> future =
                jobService.getAllJobs(page, pageSize, address, title, wage, offerType, ratingLow, ratingHigh, orderColumn, orderClause, type)
                        .thenCompose(jobCollectionPage -> jobBinder.bindOutput(jobCollectionPage, this.getClass()));

        return Mono.fromFuture(future);
    }

    @GetMapping("/{jid}")
    public Mono<OutJob> getJob(@PathVariable long jid){
        CompletableFuture<OutJob> future = jobService.getJob(jid)
                .thenCompose(jobBinder::bindOutput);

        return Mono.fromFuture(future);
    }


    @PostMapping
    public Mono<OutJob> createJob(@RequestBody InJob job, Authentication authentication){
        Job newJob = jobBinder.bindInput(job);
        CompletableFuture<OutJob> future = jobService.createJob(newJob, authentication.getName())
                .thenCompose(jobBinder::bindOutput);

        return Mono.fromFuture(future);
    }


    @PutMapping("/{jid}")
    public Mono<Void> updateJob(@PathVariable long jid, @RequestBody InJob inJob, Authentication authentication){
        if(inJob.getJobID() != jid) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Job updateJob = jobBinder.bindInput(inJob);

        return jobService.updateJob(updateJob, authentication.getName());
    }


    @DeleteMapping("/{jid}")
    public Mono<Void> deleteJob(@PathVariable long jid, Authentication authentication){
        return jobService.deleteJob(jid, authentication.getName());
    }

    @GetMapping("/{jid}/applications")
    public Mono<HalCollectionPage> getApplication(
            @PathVariable  long jid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize

    ) {
        return Mono.fromFuture(
                jobService.getApplication(jid, page, pageSize)
                        .thenCompose(applicationCollectionPage -> applicationBinder.bindOutput(applicationCollectionPage, this.getClass()))
        );
    }
}
