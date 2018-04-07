package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.JobBinder;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutJob;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.JobService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

@RestController
@RequestMapping("/accounts/jobs")
public class JobController {
    private final JobService jobService;
    private final APIService apiService;
    private final JobBinder jobBinder;

    public JobController(JobService jobService, APIService apiService, JobBinder jobBinder) {
        this.jobService = jobService;
        this.apiService = apiService;
        this.jobBinder = jobBinder;
    }

    @GetMapping
    public Resource<HalCollection> getAllJobs(@RequestParam Map<String,String> queryString){
        return jobBinder.bindOutput(
                jobService.getAllJobs(queryString),
                this.getClass()
        );
    }

    @GetMapping("/{jid}")
    public Resource<OutJob> getJob(@PathVariable long jid){
        return jobBinder.bindOutput(jobService.getJob(jid));
    }

    @PutMapping("/{jid}")
    public void updateJob(@PathVariable long jid, @RequestBody InJob job){

        if(job.getJobID() != jid) throw new BadRequestException(badRequest_IdsMismatch);
        Job updateJob = jobBinder.bindInput(job);

        jobService.updateJob(updateJob);
    }

    @PostMapping
    public void createJob( @RequestBody InJob job){
        Job newJob = jobBinder.bindInput(job);
        jobService.createJob(newJob);
    }

    @DeleteMapping("/{jid}")
    public void deleteJob(@PathVariable long jid){
        jobService.deleteJob(jid);
    }
}
