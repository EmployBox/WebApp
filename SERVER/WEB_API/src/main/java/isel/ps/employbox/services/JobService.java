package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.Transaction;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JobService {
    private final DataRepository<Job, Long> jobRepo;
    private final DataRepository<JobExperience, Long> jobExperienceRepo;
    private final DataRepository<Account, Long> accountRepo;
    private final DataRepository<Application, Long> applicationRepo;
    private final AccountService accountService;

    public JobService(DataRepository<Job, Long> jobRepo, DataRepository<JobExperience, Long> jobExperienceExperienceRepo, DataRepository<Account, Long> accountRepo, DataRepository<Application, Long> applicationRepo, AccountService userService) {
        this.jobRepo = jobRepo;
        this.jobExperienceRepo = jobExperienceExperienceRepo;
        this.accountRepo = accountRepo;
        this.applicationRepo = applicationRepo;
        this.accountService = userService;
    }

    public CompletableFuture<Stream<Job> > getAllJobs() {
        return  jobRepo.findAll().thenApply(Collection::stream);
    }

    public CompletableFuture<Job> getJob(long jid) {
        return jobRepo.findById(jid)
                .thenApply(ojob -> ojob.orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_JOB)));
    }

    public CompletableFuture<Stream<JobExperience>> getJobExperiences(long jid) {
        return jobExperienceRepo.findWhere(new Pair<>("jobId", jid))
                .thenApply(Collection::stream);
    }

    public CompletableFuture<JobExperience> getJobExperience(long id, long cid) {
        return jobExperienceRepo.findById(cid)
                .thenApply( oJobExperience -> oJobExperience.orElseThrow( ()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND)))
                .thenApply( jobExperience -> {
                    if(jobExperience.getJobId() != id)
                        throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                    return jobExperience;
                });
    }

    public CompletableFuture<Job> createJob(Job job, String email) {
        return accountService.getAccount(job.getAccountId(), email)
                .thenCompose(__ -> jobRepo.create(job))
                .thenCompose(aVoid -> job.getExperiences())
                .thenCompose(experienceList -> {
                    if (experienceList.isEmpty()) return CompletableFuture.completedFuture(null);
                    experienceList.forEach(curr -> curr.setJobId(job.getIdentityKey()));
                    return jobExperienceRepo.createAll(experienceList);
                })
                .thenApply(aVoid -> job);
    }

    public CompletableFuture<Void> addJobExperienceToJob(long jobId, List<JobExperience> jobExperience, String username){
        return getJob(jobId)
                .thenCompose(job -> accountService.getAccount(job.getAccountId(), username))
                .thenCompose(__ -> jobExperienceRepo.createAll(jobExperience));
    }

    public Mono<Void> updateJob(Job job, String email) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        getJob(job.getIdentityKey()),
                        accountService.getAccount(job.getAccountId(), email)
                )
                        .thenCompose(__ -> jobRepo.update(job))
        );
    }

    public Mono<Void> updateJobExperience(JobExperience jobExperience, String username) {
        return Mono.fromFuture(
                getJob(jobExperience.getJobId())
                        .thenCompose(job -> accountService.getAccount(job.getAccountId(), username))
                        .thenCompose(account -> jobExperienceRepo.update(jobExperience))
        );
    }

    public Mono<Void> deleteJob(long jobId, String email) {
        return Mono.fromFuture(
                accountRepo.findWhere(new Pair<>("email", email))
                        .thenApply(accounts -> {
                            if (accounts.isEmpty())
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                            return accounts.get(0);
                        })
                        .thenAccept(account -> getJob(jobId)
                                .thenCompose(job -> {
                                    if (!job.getAccountId().equals(account.getIdentityKey()))
                                        throw new BadRequestException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                                    return new Transaction(Connection.TRANSACTION_READ_UNCOMMITTED)
                                            .andDo(() -> job.getApplications()
                                                    .thenCompose(applications -> {
                                                        List<Long> applicationIds = applications.stream().map(Application::getIdentityKey).collect(Collectors.toList());
                                                        return applicationRepo.deleteAll(applicationIds);
                                                    }))
                                            .andDo(() -> job.getExperiences()
                                                    .thenCompose(jobExperiences -> {
                                                        List<Long> jobExpIds = jobExperiences.stream().map(JobExperience::getIdentityKey).collect(Collectors.toList());
                                                        return jobExperienceRepo.deleteAll(jobExpIds);
                                                    }))
                                            .andDo(() -> jobRepo.delete(job))
                                            .commit();
                                })
                                .exceptionally(throwable -> {
                                    throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                                })
                        )
        );
    }

    public Mono<Void> deleteJobExperience(long jxpId, long jobId, String email) {
        return Mono.fromFuture(
                getJob(jobId)
                        .thenCompose(job-> accountService.getAccount(job.getAccountId(), email))
                        .thenCompose(__ -> jobExperienceRepo.deleteById(jxpId))
        );
    }
}
