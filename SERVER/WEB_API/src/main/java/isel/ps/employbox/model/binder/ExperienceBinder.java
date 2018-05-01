package isel.ps.employbox.model.binder;


import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJobExperience;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class ExperienceBinder implements ModelBinder<JobExperience,OutJob.OutExperience,InJobExperience> {


    @Override
    public Mono<OutJob.OutExperience> bindOutput(CompletableFuture<JobExperience> jobExperienceCompletableFuture) {
        return Mono.fromFuture(
                jobExperienceCompletableFuture.thenApply(
                        jobExperience ->
                                new OutJob.OutExperience(jobExperience.getCompetences(),
                                        jobExperience.getYears())
                )
        );
    }

    @Override
    public JobExperience bindInput(InJobExperience object) {
        return new JobExperience( object.getYears(), object.getCompetence(),object.getYears());
    }
}
