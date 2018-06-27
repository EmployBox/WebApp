package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.Transaction;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    public CompletableFuture getAllJobs(int page, int pageSize,String address, String location, String title, Integer wage, String offerType, Integer ratingLow, Integer ratingHigh) {
        List<Pair<String, ?>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("address", address));
        //pairs.add(new Pair<>("location",location));
        pairs.add(new Pair<>("title",title));
        pairs.add(new Pair<>("wage", wage));
        pairs.add(new Pair<>("offerType", offerType));
        /*pairs.add(new Pair<>("wage", ratingLow));
        pairs.add(new Pair<>("wage", ratingHigh));*/

        Pair[] query = pairs.stream()
                .filter(stringPair -> stringPair.getValue() != null)
                .toArray(Pair[]::new);

        return ServiceUtils.getCollectionPageFuture(
                jobRepo,
                page,
                pageSize,
                query
        );
    }

    public CompletableFuture<Job> getJob(long jid) {
        return jobRepo.findById(jid)
                .thenApply(ojob -> ojob.orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_JOB)));
    }

    public CompletableFuture<CollectionPage<JobExperience>> getJobExperiences(long jid, int page, int pageSize) {
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair("jobId", jid));
        Pair[] query = pairs.stream()
                .filter(stringStringPair -> stringStringPair.getValue() != null)
                .toArray(Pair[]::new);
        return getJob(jid).thenCompose(__ -> ServiceUtils.getCollectionPageFuture(jobExperienceRepo, page, pageSize, query));
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
        return job.getAccount()
                .thenApply(account -> {
                    if (!account.getEmail().equals(email))
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return account;
                })
                .thenCompose(account -> jobRepo.create(job))
                .thenCompose(aVoid -> job.getExperiences())
                .thenCompose(experienceList -> {
                    if (experienceList.isEmpty()) return CompletableFuture.completedFuture(null);
                    experienceList.forEach(curr -> curr.setJobId(job.getIdentityKey()));
                    return jobExperienceRepo.createAll(experienceList);
                })
                .thenApply(aVoid -> job)
                .exceptionally( e -> {
                    throw new BadRequestException(e.getMessage());
                });
    }

    public CompletableFuture<Void> addJobExperienceToJob(long jobId, List<JobExperience> jobExperience, String username){
        return getJob(jobId)
                .thenCompose(job -> job.getAccount()
                        .thenApply(account -> {
                            if (!account.getEmail().equals(username))
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                            return account;
                        }))
                .thenCompose(account -> jobExperienceRepo.createAll(jobExperience));
    }

    public Mono<Void> updateJob(Job job, String email) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        getJob(job.getIdentityKey()),
                        job.getAccount()
                                .thenApply(account -> {
                                    if (!account.getEmail().equals(email))
                                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                                    return account;
                                })
                )
                        .thenCompose(aVoid -> jobRepo.update(job))
        );
    }

    public Mono<Void> updateJobExperience(JobExperience jobExperience, String username) {
        return Mono.fromFuture(
                getJob(jobExperience.getJobId())
                        .thenCompose(job -> job.getAccount()
                                .thenApply(account -> {
                                    if (!account.getEmail().equals(username))
                                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                                    return account;
                                }))
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
                        .thenCompose(account -> deleteJobAux(jobId, account))
        );
    }

    private CompletableFuture<Void> deleteJobAux(long jobId, Account account) {
        return getJob(jobId)
                .thenCompose(job -> job.getAccount().thenCompose(account1 -> {
                    if (!account1.getIdentityKey().equals(account.getIdentityKey()))
                        throw new BadRequestException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return executeDeleteTransaction(job);
                }));
    }

    private CompletableFuture<Void> executeDeleteTransaction(Job job) {
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
    }

    public Mono<Void> deleteJobExperience(long jxpId, long jobId, String email) {
        return Mono.fromFuture(
                getJob(jobId)
                        .thenCompose(job-> job.getAccount()
                                .thenApply(account -> {
                                    if (!account.getEmail().equals(email))
                                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                                    return account;
                                }))
                        .thenCompose(account -> jobExperienceRepo.deleteById(jxpId))
        );
    }
}
