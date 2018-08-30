package isel.ps.employbox.controllers.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.Jobs.JobBinder;
import isel.ps.employbox.model.entities.jobs.Job;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.input.InSchedule;
import isel.ps.employbox.model.output.HalCollectionPage;
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
    private final JobBinder jobBinder;

    public JobController(JobService jobService, JobBinder jobBinder) {
        this.jobService = jobService;
        this.jobBinder = jobBinder;
    }
    //todo application
    @GetMapping
    public Mono<HalCollectionPage<Job>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer wage,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String offerType,
            @RequestParam(required = false) Integer ratingLow,
            @RequestParam(required = false) Integer ratingHigh,
            @RequestParam(required = false) String schedules,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    ) throws IOException {
        if (schedules != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            List<InSchedule> someClassList = objectMapper.readValue(schedules, typeFactory.constructCollectionType(List.class, InSchedule.class));
        }

        CompletableFuture<HalCollectionPage<Job>> future = jobService.getAllJobs(page, pageSize, address, title, wage, offerType, ratingLow, ratingHigh, orderColumn, orderClause, type)
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

}
