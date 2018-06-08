package isel.ps.employbox.model.binders;


import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJobExperience;
import isel.ps.employbox.model.output.OutJobExperience;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class JobExperienceBinder implements ModelBinder<JobExperience,OutJobExperience,InJobExperience> {

    @Override
    public Mono<OutJobExperience> bindOutput(CompletableFuture<JobExperience> jobExperienceCompletableFuture) {
        return Mono.fromFuture(
                jobExperienceCompletableFuture.thenApply(
                        jobExperience ->
                                new OutJobExperience(
                                        jobExperience.getIdentityKey(),
                                        jobExperience.getJobId(),
                                        jobExperience.getCompetences(),
                                        jobExperience.getYears())
                )
        );
    }

    @Override
    public JobExperience bindInput(InJobExperience inJobExperience) {
        return new JobExperience(inJobExperience.getJobExperienceId(), inJobExperience.getJobId(), inJobExperience.getCompetences(), inJobExperience.getYears(),
                inJobExperience.getVersion());
    }
}