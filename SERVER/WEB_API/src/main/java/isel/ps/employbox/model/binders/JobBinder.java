package isel.ps.employbox.model.binders;

import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class JobBinder implements ModelBinder<Job, OutJob, InJob> {
    private final AccountBinder accountBinder;

    public JobBinder(AccountBinder accountBinder) {
        this.accountBinder = accountBinder;
    }

    @Override
    public Mono<OutJob> bindOutput(CompletableFuture<Job> jobCompletableFuture) {
        Job [] jobVar = new Job[1];
        return Mono.fromFuture(
                jobCompletableFuture
                        .thenCompose(job ->{
                            jobVar[0] = job;
                            return job.getAccountToOutput();
                        })
                        .thenApply(outAccount -> new OutJob(
                                        outAccount,
                                        jobVar[0].getIdentityKey(),
                                        jobVar[0].getTitle(),
                                        jobVar[0].getAddress(),
                                        jobVar[0].getWage(),
                                        jobVar[0].getDescription(),
                                        jobVar[0].getSchedule(),
                                        jobVar[0].getOfferBeginDate(),
                                        jobVar[0].getOfferEndDate(),
                                        jobVar[0].getOfferType()
                        ))
        );
    }

    @Override
    public Job bindInput(InJob inJob) {
        return new Job(
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
