package isel.ps.employbox.controllers;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.JobBinder;
import isel.ps.employbox.model.binder.JobExperienceBinder;
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
    public Mono<HalCollectionPage> getAllJobs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int numberOfItems){
        return jobBinder.bindOutput(
                jobService.getAllJobs(page, numberOfItems),
                this.getClass()
        );
    }

    @GetMapping("/{jid}")
    public Mono<OutJob> getJob(@PathVariable long jid){
        return jobBinder.bindOutput(
                jobService.getJob(jid)
        );
    }

    @GetMapping("/{jid}/experiences")
    public Mono<HalCollectionPage> getJobExperiences(@PathVariable long jid, @RequestParam(defaultValue = "0") int page){
        return jobExperienceBinder.bindOutput(
                jobService.getJobExperiences(jid, page),
                this.getClass(),
                jid
        );
    }

    @GetMapping("/{jid}/experiences/{expId}")
    public Mono<OutJobExperience> getJobExperience(
            @PathVariable long jid,
            @PathVariable long expId){
        return jobExperienceBinder.bindOutput(jobService.getJobExperience(jid, expId));
    }

    @PostMapping
    public Mono<OutJob> createJob(@RequestBody InJob job, Authentication authentication){
        Job newJob = jobBinder.bindInput(job);
        return jobBinder.bindOutput(jobService.createJob(newJob, authentication.getName()));
    }

    @PostMapping("/{jid}/experiences")
    public Mono<Void> createJobExperiences(
            @PathVariable long jid,
            @RequestBody List<JobExperience> jobExperience,
            Authentication authentication
    ) {
        return Mono.fromFuture( jobService.addJobExperienceToJob(jid, jobExperience, authentication.getName()));
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

    @DeleteMapping("/{jid}/experience/{jxpId}")
    public Mono<Void> deleteJobExperiences(
            @PathVariable long jxpId,
            @PathVariable long jid,
            Authentication authentication)
    {
        return jobService.deleteJobExperience(jxpId, jid, authentication.getName());
    }

    @DeleteMapping("/{jid}")
    public Mono<Void> deleteJob(@PathVariable long jid, Authentication authentication){
        return jobService.deleteJob(jid, authentication.getName());
    }
}
