package isel.ps.employbox.model.binder;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class JobBinder implements ModelBinder<Job, OutJob, InJob> {
    private final DataRepository<Account, Long> accountRepo;
    private final AccountBinder accountBinder;

    public JobBinder(DataRepository<Account, Long> accountRepo, AccountBinder accountBinder) {
        this.accountRepo = accountRepo;
        this.accountBinder = accountBinder;
    }

    @Override
    public Mono<OutJob> bindOutput(CompletableFuture<Job> jobCompletableFuture) {
        return Mono.fromFuture(
                jobCompletableFuture
                        .thenCompose(job -> accountBinder.bindOutput(job.getAccount())
                                .map(outAccount -> new OutJob(
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
                                ))
                                .toFuture()
                        )
        );
    }

    @Override
    public Job bindInput(InJob inJob) {
        CompletableFuture<Account> accountCF = accountRepo.findById(inJob.getAccountId())
                .thenApply(account -> account.orElseThrow(() -> new ResourceNotFoundException("Account not found")));

        return new Job(
                accountCF,
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
