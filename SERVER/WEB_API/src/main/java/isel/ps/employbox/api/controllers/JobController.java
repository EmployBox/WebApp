package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.exceptions.BadRequestException;
import isel.ps.employbox.api.model.binder.JobBinder;
import isel.ps.employbox.api.model.input.InJob;
import isel.ps.employbox.api.model.output.OutJob;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.AccountService;
import isel.ps.employbox.api.services.JobService;
import isel.ps.employbox.dal.model.Job;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static isel.ps.employbox.api.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
public class JobController {
    private final JobService jobService;
    private final APIService apiService;
    private final JobBinder jobBinder;

    public JobController(JobService jobService, APIService apiService, JobBinder jobBinder) {
        this.jobService = jobService;
        this.apiService = apiService;
        this.jobBinder = jobBinder;
    }

    @GetMapping("/jobs")
    public List<OutJob> getAllJobs(@RequestParam Map<String,String> queryString){
        return jobBinder.bindOutput(
                jobService.getAllJobs(queryString)
        );
    }

    @GetMapping("/account/{id}/job")
    public List<OutJob> getAccountOffers(@PathVariable long id){
        return jobBinder.bindOutput(
                jobService.getAccountOffers(id)
        );
    }

    @GetMapping("/account/{id}/job/{jid}")
    public Optional<OutJob> getJob(@PathVariable long id, @PathVariable long jid){
        return jobService.getJob(id, jid).map(jobBinder::bindOutput);
    }

    @PutMapping("/account/{id}/job/{jid}")
    public void updateJob(@PathVariable long id, @PathVariable long jid, @RequestHeader String apiKey, @RequestBody InJob job){
        apiService.validateAPIKey(apiKey);

        if(job.getAccountID() != id || job.getJobID() != jid) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Job updateJob = jobBinder.bindInput(job);

        jobService.updateJob(updateJob);
    }

    @PostMapping("/account/{id}/job")
    public void createJob(@PathVariable long id, @RequestHeader String apiKey, @RequestBody InJob job){
        apiService.validateAPIKey(apiKey);

        if(job.getAccountID() != id) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Job newJob = jobBinder.bindInput(job);

        jobService.createJob(newJob);
    }

    @DeleteMapping("/account/{id}/job/{jid}")
    public void deleteJob(@PathVariable long id, @PathVariable long jid, @RequestHeader String apiKey){
        apiService.validateAPIKey(apiKey);
        jobService.deleteJob(id, jid);
    }
}
