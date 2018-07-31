package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import com.github.jayield.rapper.utils.UnitOfWork;
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

    public CompletableFuture<CollectionPage<Job>> getAllJobs(int page, int pageSize,String address, String location, String title, Integer wage, String offerType, Integer ratingLow, Integer ratingHigh) {
        List<Pair<String, Object>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("address", address));
        //pairs.add(new Pair<>("location",location));
        pairs.add(new Pair<>("title",title));
        pairs.add(new Pair<>("wage", wage));
        pairs.add(new Pair<>("offerType", offerType));
        /*pairs.add(new Pair<>("wage", ratingLow));
        pairs.add(new Pair<>("wage", ratingHigh));*/

        pairs = pairs.stream()
                .filter(stringPair -> stringPair.getValue() != null)
                .collect(Collectors.toList());

        return ServiceUtils.getCollectionPageFuture(
                jobRepo,
                page,
                pageSize,
                pairs.toArray(new Pair[pairs.size()])
        );
    }

    public CompletableFuture<Job> getJob(long jid) {
        UnitOfWork unitOfWork = new UnitOfWork();

        return jobRepo.findById(unitOfWork, jid)
                .thenCompose(res -> unitOfWork.commit().thenApply( aVoid -> res))
                .thenApply(ojob -> ojob.orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_JOB)));
    }

    public CompletableFuture<CollectionPage<JobExperience>> getJobExperiences(long jid, int page, int pageSize) {
        List<Pair<String, Object>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("jobId", jid));
        Pair[] query = pairs.stream()
                .filter(stringStringPair -> stringStringPair.getValue() != null)
                .toArray(Pair[]::new);
        return getJob(jid).thenCompose(ignored -> ServiceUtils.getCollectionPageFuture(jobExperienceRepo, page, pageSize, query));
    }

    public CompletableFuture<JobExperience> getJobExperience(long id, long cid) {
        UnitOfWork unitOfWork = new UnitOfWork();

        return jobExperienceRepo.findById(unitOfWork, cid)
                .thenCompose( res -> unitOfWork.commit().thenApply( aVoid -> res))
                .thenApply( oJobExperience -> oJobExperience.orElseThrow( ()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND)))
                .thenApply( jobExperience -> {
                    if(jobExperience.getJobId() != id)
                        throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                    return jobExperience;
                });
    }

    public CompletableFuture<Job> createJob(Job job, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();

        return job.getAccount().getForeignObject(unitOfWork)
                .thenApply(account -> {
                    if (!account.getEmail().equals(email))
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return account;
                })
                .thenCompose(account -> jobRepo.create(unitOfWork, job))
                .thenCompose(aVoid -> job.getExperiences().apply(unitOfWork))
                .thenCompose(experienceList -> {
                    if (experienceList.isEmpty()) return CompletableFuture.completedFuture(null);

                    experienceList.forEach(curr -> curr.setJobId(job.getIdentityKey()));

                    return jobExperienceRepo.createAll(unitOfWork, experienceList);
                })
                .thenCompose(ignored -> unitOfWork.commit().thenApply(aVoid -> job))
                .exceptionally( e -> {
                    throw new BadRequestException(e.getMessage());
                });
    }

    public CompletableFuture<Void> addJobExperienceToJob(long jobId, List<JobExperience> jobExperience, String username){
        UnitOfWork unitOfWork = new UnitOfWork();

        return getJob(jobId)
                .thenCompose(job -> job.getAccount().getForeignObject(unitOfWork))
                .thenApply(
                        account -> {
                            if (!account.getEmail().equals(username))
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                            return account;
                        })
                .thenCompose(account -> jobExperienceRepo.createAll(unitOfWork, jobExperience))
                .thenCompose(aVoid -> unitOfWork.commit());
    }

    public Mono<Void> updateJob(Job job, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        getJob(job.getIdentityKey()),
                        job.getAccount().getForeignObject(unitOfWork)
                                .thenApply(account -> {
                                    if (!account.getEmail().equals(email))
                                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                                    return account;
                                })
                )
                        .thenCompose(aVoid -> jobRepo.update(unitOfWork, job))
                        .thenCompose(aVoid -> unitOfWork.commit())
        );
    }

    public Mono<Void> updateJobExperience(JobExperience jobExperience, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return Mono.fromFuture(
                getJob(jobExperience.getJobId())
                        .thenCompose(job -> job.getAccount().getForeignObject(unitOfWork)
                                .thenApply(account -> {
                                    if (!account.getEmail().equals(username))
                                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                                    return account;
                                }))
                        .thenCompose(account -> jobExperienceRepo.update(unitOfWork, jobExperience))
                        .thenCompose(aVoid -> unitOfWork.commit())
        );
    }

    public Mono<Void> deleteJob(long jobId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return Mono.fromFuture(
                accountRepo.findWhere(unitOfWork, new Pair<>("email", email))
                        .thenCompose( res -> unitOfWork.commit().thenApply( aVoid -> res))
                        .thenApply(accounts -> {
                            if (accounts.isEmpty())
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                            return accounts.get(0);
                        })
                        .thenCompose(account -> deleteJobAux(jobId, account))
        );
    }
    //todo commit
    private CompletableFuture<Void> deleteJobAux(long jobId, Account account) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return getJob(jobId)
                .thenCompose(
                        job -> job.getAccount()
                                .getForeignObject(unitOfWork)
                                .thenCompose(acc -> {
                                    if (!acc.getIdentityKey().equals(account.getIdentityKey()))
                                        throw new BadRequestException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                                    return executeDeleteTransaction(job);
                                })
                );
    }

    private CompletableFuture<Void> executeDeleteTransaction(Job job) {
        UnitOfWork unitOfWork = new UnitOfWork();

        return job.getApplications().apply(unitOfWork)
                .thenCompose(applications -> {
                    List<Long> applicationIds = applications.stream().map(Application::getIdentityKey).collect(Collectors.toList());
                    return applicationRepo.deleteAll(unitOfWork, applicationIds);
                })
                .thenCompose(aVoid ->
                        job.getExperiences()
                                .apply(unitOfWork)
                                .thenCompose(jobExperiences -> {
                                    List<Long> jobExpIds = jobExperiences.stream().map(JobExperience::getIdentityKey).collect(Collectors.toList());
                                    return jobExperienceRepo.deleteAll(unitOfWork, jobExpIds);
                                })
                )
                .thenAccept(aVoid -> jobRepo.delete(unitOfWork, job))
                .thenCompose(aVoid -> unitOfWork.commit());
    }

    public Mono<Void> deleteJobExperience(long jxpId, long jobId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();

        return Mono.fromFuture(
                getJob(jobId)
                        .thenCompose(job-> job.getAccount().getForeignObject(unitOfWork)
                                .thenApply(account -> {
                                    if (!account.getEmail().equals(email))
                                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                                    return account;
                                }))
                        .thenCompose(account -> jobExperienceRepo.deleteById(unitOfWork, jxpId))
                        .thenCompose( aVoid -> unitOfWork.commit())
        );
    }
}
