package isel.ps.employbox.controllers;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.JobBinder;
import isel.ps.employbox.model.binders.JobExperienceBinder;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.input.InJobExperience;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutJob;
import isel.ps.employbox.model.output.OutJobExperience;
import isel.ps.employbox.services.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;
    private final JobBinder jobBinder;
    private final JobExperienceBinder jobExperienceBinder;

    public JobController(JobService jobService, JobBinder jobBinder, JobExperienceBinder jobExperienceBinder) {
        this.jobService = jobService;
        this.jobBinder = jobBinder;
        this.jobExperienceBinder = jobExperienceBinder;
    }

    @GetMapping
    public Mono<HalCollectionPage<Job>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer wage,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer ratingLow,
            @RequestParam(required = false) Integer ratingHigh
    ){
        CompletableFuture<HalCollectionPage<Job>> future = jobService.getAllJobs(page, pageSize, address, title, wage, type, ratingLow, ratingHigh)
                .thenCompose(jobCollectionPage -> jobBinder.bindOutput(jobCollectionPage, this.getClass()));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{jid}")
    public Mono<OutJob> getJob(@PathVariable long jid){
        CompletableFuture<OutJob> future = jobService.getJob(jid)
                .thenCompose(jobBinder::bindOutput);

        return Mono.fromFuture(future);
    }

    @GetMapping("/{jid}/experiences")
    public Mono<HalCollectionPage<JobExperience>> getJobExperiences(
            @PathVariable long jid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        CompletableFuture<HalCollectionPage<JobExperience>> future = jobService.getJobExperiences(jid, page, pageSize)
                .thenCompose(jobExperienceCollectionPage -> jobExperienceBinder.bindOutput(jobExperienceCollectionPage, this.getClass(), jid));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{jid}/experiences/{expId}")
    public Mono<OutJobExperience> getJobExperience(
            @PathVariable long jid,
            @PathVariable long expId
    ){
        CompletableFuture<OutJobExperience> future = jobService.getJobExperience(jid, expId)
                .thenCompose(jobExperienceBinder::bindOutput);

        return Mono.fromFuture(future);
    }

    @PostMapping
    public Mono<OutJob> createJob(@RequestBody InJob job, Authentication authentication){
        Job newJob = jobBinder.bindInput(job);
        CompletableFuture<OutJob> future = jobService.createJob(newJob, authentication.getName())
                .thenCompose(jobBinder::bindOutput);

        return Mono.fromFuture(future);
    }

    @PostMapping("/{jid}/experiences")
    public Mono<Void> createJobExperiences(
            @PathVariable long jid,
            @RequestBody List<InJobExperience> inJobExperiences,
            Authentication authentication
    ) {
        List<JobExperience> jobExperiences = inJobExperiences.stream().map(jobExperienceBinder::bindInput).collect(Collectors.toList());
        CompletableFuture<Void> future = jobService.addJobExperienceToJob(jid, jobExperiences, authentication.getName());
        return Mono.fromFuture(future);
    }

    @PutMapping("/{jid}")
    public Mono<Void> updateJob(@PathVariable long jid, @RequestBody InJob inJob, Authentication authentication){
        if(inJob.getJobID() != jid) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Job updateJob = jobBinder.bindInput(inJob);

        return jobService.updateJob(updateJob, authentication.getName());
    }

    @PutMapping("/{jid}/experiences/{jxpId}")
    public Mono<Void> updateJobExperiences(
            @PathVariable long jxpId,
            @PathVariable long jid,
            @RequestBody InJobExperience inJobExperience,
            Authentication authentication
    ) {
        if(jid != inJobExperience.getJobId() || jxpId != inJobExperience.getJobExperienceId())
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

        JobExperience jobExperience = jobExperienceBinder.bindInput(inJobExperience);

        return jobService.updateJobExperience(jobExperience, authentication.getName());
    }

    @DeleteMapping("/{jid}")
    public Mono<Void> deleteJob(@PathVariable long jid, Authentication authentication){
        return jobService.deleteJob(jid, authentication.getName());
    }

    @DeleteMapping("/{jid}/experience/{jxpId}")
    public Mono<Void> deleteJobExperiences(
            @PathVariable long jxpId,
            @PathVariable long jid,
            Authentication authentication)
    {
        return jobService.deleteJobExperience(jxpId, jid, authentication.getName());
    }
}
