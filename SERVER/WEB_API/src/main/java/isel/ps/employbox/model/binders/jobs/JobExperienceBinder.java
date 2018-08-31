package isel.ps.employbox.model.binders.jobs;

import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.jobs.JobExperience;
import isel.ps.employbox.model.input.InJobExperience;
import isel.ps.employbox.model.output.OutJobExperience;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class JobExperienceBinder implements ModelBinder<JobExperience,OutJobExperience,InJobExperience> {

    @Override
    public CompletableFuture<OutJobExperience> bindOutput(JobExperience jobExperience) {
        UnitOfWork unitOfWork = new UnitOfWork();

        return jobExperience.getJob()
                .getForeignObject(unitOfWork)
                .thenCompose(res -> unitOfWork.commit().thenApply(__ -> res))
                .thenApply(job -> new OutJobExperience(
                            jobExperience.getIdentityKey(),
                            job.getIdentityKey(),
                            jobExperience.getCompetences(),
                            jobExperience.getYears()
                ));
    }

    @Override
    public JobExperience bindInput(InJobExperience inJobExperience) {
        return new JobExperience(
                inJobExperience.getJobExperienceId(),
                inJobExperience.getJobId(),
                inJobExperience.getCompetences(),
                inJobExperience.getYears(),
                inJobExperience.getVersion()
        );
    }
}
