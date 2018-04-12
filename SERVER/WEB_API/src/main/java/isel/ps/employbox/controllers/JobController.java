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
    public HalCollection getAllJobs(){
        return jobBinder.bindOutput(
                jobService.getAllJobs(),
                this.getClass()
        );
    }

    @GetMapping("/{jid}")
    public OutJob getJob(@PathVariable long jid){
        return jobBinder.bindOutput(jobService.getJob(jid));
    }

    @PutMapping("/{jid}")
    public void updateJob(@PathVariable long jid, @RequestBody InJob job, Authentication authentication){

        if(job.getJobID() != jid) throw new BadRequestException(badRequest_IdsMismatch);
        Job updateJob = jobBinder.bindInput(job);

        jobService.updateJob(updateJob, authentication.getName());
    }

    @PostMapping
    public void createJob( @RequestBody InJob job, Authentication authentication){
        Job newJob = jobBinder.bindInput(job);
        jobService.createJob(newJob, authentication.getName());
    }

    @DeleteMapping("/{jid}")
    public void deleteJob(@PathVariable long jid, Authentication authentication){
        jobService.deleteJob(jid, authentication.getName() );
    }
}
