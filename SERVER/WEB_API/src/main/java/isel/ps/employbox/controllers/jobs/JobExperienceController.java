package isel.ps.employbox.controllers.jobs;


import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.jobs.JobExperienceBinder;
import isel.ps.employbox.model.entities.jobs.JobExperience;
import isel.ps.employbox.model.input.InJobExperience;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutJobExperience;
import isel.ps.employbox.services.JobService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobs/{jid}/experiences")
public class JobExperienceController {
    private final JobService jobService;
    private final JobExperienceBinder jobExperienceBinder;

    public JobExperienceController(JobService jobService, JobExperienceBinder jobExperienceBinder) {
        this.jobService = jobService;
        this.jobExperienceBinder = jobExperienceBinder;
    }

    @GetMapping
    public Mono<HalCollectionPage<JobExperience>> getJobExperiences(
            @PathVariable long jid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        CompletableFuture<HalCollectionPage<JobExperience>> future = jobService.getJobExperiences(jid, page, pageSize)
                .thenCompose(jobExperienceCollectionPage -> jobExperienceBinder.bindOutput(jobExperienceCollectionPage, this.getClass(), jid));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{expId}")
    public Mono<OutJobExperience> getJobExperience(
            @PathVariable long jid,
            @PathVariable long expId
    ){
        CompletableFuture<OutJobExperience> future = jobService.getJobExperience(jid, expId)
                .thenCompose(jobExperienceBinder::bindOutput);

        return Mono.fromFuture(future);
    }

    @PostMapping
    public Mono<Void> createJobExperiences(
            @PathVariable long jid,
            @RequestBody List<InJobExperience> inJobExperiences,
            Authentication authentication
    ) {
        List<JobExperience> jobExperiences = inJobExperiences.stream().map(jobExperienceBinder::bindInput).collect(Collectors.toList());
        CompletableFuture<Void> future = jobService.addJobExperienceToJob(jid, jobExperiences, authentication.getName());
        return Mono.fromFuture(future);
    }

    @PutMapping("/{jxpId}")
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

    @DeleteMapping("/{jxpId}")
    public Mono<Void> deleteJobExperiences(
            @PathVariable long jxpId,
            @PathVariable long jid,
            Authentication authentication)
    {
        return jobService.deleteJobExperience(jxpId, jid, authentication.getName());
    }

}
