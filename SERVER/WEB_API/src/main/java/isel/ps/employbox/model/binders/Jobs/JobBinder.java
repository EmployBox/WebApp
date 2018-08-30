package isel.ps.employbox.model.binders.Jobs;

import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.jobs.Application;
import isel.ps.employbox.model.entities.jobs.Job;
import isel.ps.employbox.model.entities.jobs.JobExperience;
import isel.ps.employbox.model.entities.jobs.Schedule;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Component
public class JobBinder implements ModelBinder<Job, OutJob, InJob> {
    @Override
    public CompletableFuture<OutJob> bindOutput(Job job) {
        UnitOfWork unitOfWork = new UnitOfWork();
        AccountBinder accountBinder = new AccountBinder();

        CompletableFuture<OutJob> future = job.getAccount()
                .getForeignObject(unitOfWork)
                .thenCompose(account1 -> unitOfWork.commit().thenApply(aVoid -> account1))
                .thenCompose(accountBinder::bindOutput)
                .thenApply(outAccount -> new OutJob(
                        outAccount,
                        job.getIdentityKey(),
                        job.getTitle(),
                        job.getAddress(),
                        job.getWage(),
                        job.getDescription(),
                        job.getOfferBeginDate(),
                        job.getOfferEndDate(),
                        job.getOfferType(),
                        job.getType()));

        return handleExceptions(future, unitOfWork);
    }

    @Override
    public Job bindInput(InJob inJob) {
        /*JobExperienceBinder jobExperienceBinder = new JobExperienceBinder();
        ApplicationBinder applicationBinder = new ApplicationBinder();
        ScheduleBinder scheduleBinder = new ScheduleBinder();

        List<JobExperience> jobExperiences = jobExperienceBinder.bindInput(inJob.getExperiences().stream()).collect(Collectors.toList());
        List<Application> applications = applicationBinder.bindInput(inJob.getApplications().stream()).collect(Collectors.toList());
        List<Schedule> schedules = scheduleBinder.bindInput(inJob.getSchedules().stream()).collect(Collectors.toList());*/

        return new Job(
                inJob.getAccountId(),
                inJob.getJobID(),
                inJob.getTitle(),
                inJob.getAddress(),
                inJob.getWage(),
                inJob.getDescription(),
                inJob.getOfferBeginDate(),
                inJob.getOfferEndDate(),
                inJob.getOfferType(),
                inJob.getType(),
                inJob.getApplications(),
                inJob.getExperiences(),
                inJob.getSchedules(),
                inJob.getVersion()
        );
    }
}
