package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.JobBinder;
import isel.ps.employbox.model.binder.JobExperienceBinder;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutJob;
import isel.ps.employbox.model.output.OutJobExperience;
import isel.ps.employbox.services.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

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
    public Mono<HalCollection> getAllJobs(){
        return jobBinder.bindOutput(
                jobService.getAllJobs(),
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
    public Mono<HalCollection> getJobExperiences(@PathVariable long jid){
        return jobExperienceBinder.bindOutput(
                jobService.getJobExperiences(jid),
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

    @PutMapping("/{jid}")
    public Mono<Void> updateJob(@PathVariable long jid, @RequestBody InJob job, Authentication authentication){
        if(job.getJobID() != jid) throw new BadRequestException(badRequest_IdsMismatch);
        Job updateJob = jobBinder.bindInput(job);

        return jobService.updateJob(updateJob, authentication.getName());
    }

    @PostMapping
    public Mono<OutJob> createJob(@RequestBody InJob job, Authentication authentication){
        Job newJob = jobBinder.bindInput(job);
        return jobBinder.bindOutput(jobService.createJob(newJob, authentication.getName()));
    }

    @PostMapping("/{jid}/experience")
    public Mono<Void> createJobExperiences(
            @PathVariable long jid,
            @RequestBody List<JobExperience> jobExperience,
            Authentication authentication)
    {
        return Mono.fromFuture( jobService.addJobExperienceToJob(jid, jobExperience, authentication.getName()));

    }

    @PutMapping("/{jid}/experience/{jxpId}")
    public Mono<Void> updateJobExperiences(
            @PathVariable long jxpId,
            @PathVariable long jid,
            @RequestBody JobExperience jobExperience,
            Authentication authentication)
    {
        return jobService.updateJobExperience(jxpId, jid, jobExperience, authentication.getName());
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
        return jobService.deleteJob(jid, authentication.getName() );
    }
}
