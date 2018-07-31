package isel.ps.employbox.model.binders;

import com.github.jayield.rapper.utils.UnitOfWork;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class JobBinder implements ModelBinder<Job, OutJob, InJob> {
    @Override
    public CompletableFuture<OutJob> bindOutput(Job job) {
        UnitOfWork unitOfWork = new UnitOfWork();
        AccountBinder accountBinder = new AccountBinder();

        return job.getAccount()
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
                        job.getSchedule(),
                        job.getOfferBeginDate(),
                        job.getOfferEndDate(),
                        job.getOfferType()
                ));
    }

    @Override
    public Job bindInput(InJob inJob) {
        return new Job(
                inJob.getAccountId(),
                inJob.getJobID(),
                inJob.getTitle(),
                inJob.getAddress(),
                inJob.getWage(),
                inJob.getDescription(),
                inJob.getSchedule(),
                inJob.getOfferBeginDate(),
                inJob.getOfferEndDate(),
                inJob.getOfferType(),
                inJob.getApplications(),
                inJob.getExperiences(),
                inJob.getVersion()
        );
    }
}
