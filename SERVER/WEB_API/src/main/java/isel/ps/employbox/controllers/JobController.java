package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.JobBinder;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutJob;
import isel.ps.employbox.services.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;
    private final JobBinder jobBinder;

    public JobController(JobService jobService, JobBinder jobBinder) {
        this.jobService = jobService;
        this.jobBinder = jobBinder;
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

    @PutMapping("/{jid}")
    public Mono<Void> updateJob(@PathVariable long jid, @RequestBody InJob job, Authentication authentication){

        if(job.getJobID() != jid) throw new BadRequestException(badRequest_IdsMismatch);
        Job updateJob = jobBinder.bindInput(job);

        return jobService.updateJob(updateJob, authentication.getName());
    }


    @PostMapping
    public Mono<OutJob> createJob( @RequestBody InJob job, Authentication authentication){
        Job newJob = jobBinder.bindInput(job);
        return jobBinder.bindOutput(jobService.createJob(newJob, authentication.getName()));
    }

    @DeleteMapping("/{jid}")
    public Mono<Void> deleteJob(@PathVariable long jid, Authentication authentication){
        return jobService.deleteJob(jid, authentication.getName() );
    }
}
