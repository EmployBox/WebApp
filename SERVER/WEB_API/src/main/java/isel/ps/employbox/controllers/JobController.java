package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.JobBinder;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.JobService;
import isel.ps.employbox.model.entities.Job;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

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
    public List<OutJob> getAllJobs(@RequestParam Map<String,String> queryString){
        return jobBinder.bindOutput(
                jobService.getAllJobs(queryString)
        );
    }

    @GetMapping("/{jid}")
    public Optional<OutJob> getJob(@PathVariable long jid){
        return jobService.getJob(jid).map(jobBinder::bindOutput);
    }

    @PutMapping("/{jid}")
    public void updateJob(@PathVariable long jid, @RequestHeader String apiKey, @RequestBody InJob job){
        apiService.validateAPIKey(apiKey);

        if(job.getJobID() != jid) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Job updateJob = jobBinder.bindInput(job);

        jobService.updateJob(updateJob);
    }

    @PostMapping
    public void createJob(@RequestHeader String apiKey, @RequestBody InJob job){
        apiService.validateAPIKey(apiKey);
        Job newJob = jobBinder.bindInput(job);
        jobService.createJob(newJob);
    }

    @DeleteMapping("/{jid}")
    public void deleteJob(@PathVariable long jid, @RequestHeader String apiKey){
        apiService.validateAPIKey(apiKey);
        jobService.deleteJob(jid);
    }
}
