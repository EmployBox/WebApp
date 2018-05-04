package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class JobBinder implements ModelBinder<Job,OutJob,InJob> {

    @Override
    public Mono<OutJob> bindOutput(CompletableFuture<Job> jobCompletableFuture) {
        return Mono.fromFuture(
                jobCompletableFuture
                        .thenApply(job-> new OutJob(
                                    job.getAccountId(),
                                    job.getIdentityKey(),
                                    job.getTitle(),
                                    job.getAddress(),
                                    job.getWage(),
                                    job.getDescription(),
                                    job.getSchedule(),
                                    job.getOfferBeginDate(),
                                    job.getOfferEndDate(),
                                    job.getOfferType()
                        )
                )
        );
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
                inJob.getExperiences(),
                inJob.getVersion()
        );
    }
}
