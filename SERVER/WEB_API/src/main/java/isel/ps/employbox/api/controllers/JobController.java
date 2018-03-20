package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.input.InJob;
import isel.ps.employbox.api.model.output.OutJob;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.JobService;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.dal.model.Job;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class JobController implements ModelBinder<Job, OutJob, InJob, Long>{
    private final JobService jobService;
    private final APIService apiService;

    public JobController(JobService jobService, APIService apiService) {
        this.jobService = jobService;
        this.apiService = apiService;
    }

    @Override
    public List<OutJob> bindOutput(List<Job> list) {
        return null;
    }

    @Override
    public List<Job> bindInput(List<InJob> list) {
        return null;
    }

    @Override
    public OutJob bindOutput(Job object) {
        return null;
    }

    @Override
    public Job bindInput(InJob object) {
        return null;
    }

    @GetMapping("/account/{id}/job/{jid}")
    public Optional<OutJob> getJob(@PathVariable long id, @PathVariable long jid){
        return bindOutput(Collections.singletonList(jobService.getJob(id, jid)))
                .stream()
                .findFirst();
    }

    @PutMapping("/account/{id}/job/{jid}")
    public void updateJob(@PathVariable long id, @PathVariable long jid, @RequestHeader String apiKey, @RequestBody InJob job){
        apiService.validateAPIKey(apiKey);

        job.setAccountID(id);
        job.setJobID(jid);
        Job updateJob = bindInput(job);

        jobService.updateJob(updateJob);
    }

    @PostMapping("/account/{id}/job")
    public void createJob(@PathVariable long id, @RequestHeader String apiKey, @RequestBody InJob job){
        apiService.validateAPIKey(apiKey);

        job.setAccountID(id);
        Job newJob = bindInput(job);

        jobService.createJob(newJob);
    }

    @DeleteMapping("/account/:id/job/:jid")
    public void deleteJob(@PathVariable long id, @PathVariable long jid, @RequestHeader String apiKey){
        apiService.validateAPIKey(apiKey);
        jobService.deleteJob(id, jid);
    }
}
