package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualCondition;
import com.github.jayield.rapper.mapper.conditions.LikeCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class JobService {

    public CompletableFuture<CollectionPage<Job>> getAllJobs(int page, int pageSize, String address, String location, String title, Integer wage, String offerType, Integer ratingLow, Integer ratingHigh) {
        List<Condition> pairs = new ArrayList<>();
        pairs.add(new LikeCondition("address", address));
        pairs.add(new EqualCondition<>("location", location));
        pairs.add(new EqualCondition<>("wage", wage));
        pairs.add(new EqualCondition<>("wage", wage));
        pairs.add(new EqualCondition<>("offerType", offerType));
        pairs.add(new Condition<>("ratingLow",">", ratingLow));
        pairs.add(new Condition<>("ratingHigh","<", ratingHigh));

        pairs = pairs.stream()
                .filter(stringPair -> stringPair.getValue() != null)
                .collect(Collectors.toList());

        return ServiceUtils.getCollectionPageFuture(
                Job.class,
                page,
                pageSize,
                pairs.toArray(new Condition[pairs.size()])
        );
    }

    public CompletableFuture<Job> getJob(long jid) {
        UnitOfWork unitOfWork = new UnitOfWork();
        return getJob(jid, unitOfWork)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res));
    }

    public CompletableFuture<Job> getJob(long jid, UnitOfWork unitOfWork) {
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);
        CompletableFuture<Job> future = jobMapper.findById(jid)
                .thenApply(ojob -> ojob.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_JOB)));
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<CollectionPage<JobExperience>> getJobExperiences(long jid, int page, int pageSize) {
        List<Condition< Long>> pairs = new ArrayList<>();
        pairs.add(new EqualCondition<>("jobId", jid));
        Condition[] query = pairs.stream()
                .filter(stringStringPair -> stringStringPair.getValue() != null)
                .toArray(Condition[]::new);
        return getJob(jid).thenCompose(ignored -> ServiceUtils.getCollectionPageFuture(JobExperience.class, page, pageSize, query));
    }

    public CompletableFuture<JobExperience> getJobExperience(long id, long cid) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);

        CompletableFuture<JobExperience> future = jobExperienceMapper.findById( cid)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(oJobExperience -> oJobExperience.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND)))
                .thenApply(jobExperience -> {
                    if (jobExperience.getJobId() != id)
                        throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                    return jobExperience;
                });
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<Job> createJob(Job job, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);
        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        CompletableFuture<Job> future = job.getAccount().getForeignObject(unitOfWork)
                .thenApply(account -> {
                    if (!account.getEmail().equals(email))
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return account;
                })
                .thenCompose(account -> jobMapper.create( job))
                .thenCompose(aVoid -> job.getExperiences().apply(unitOfWork))
                .thenCompose(experienceList -> {
                    if (experienceList.isEmpty()) return CompletableFuture.completedFuture(null);

                    experienceList.forEach(curr -> curr.setJobId(job.getIdentityKey()));

                    return jobExperienceMapper.createAll(experienceList);
                })
                .thenCompose(ignored -> unitOfWork.commit().thenApply(aVoid -> job));
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<Void> addJobExperienceToJob(long jobId, List<JobExperience> jobExperience, String username){
        UnitOfWork unitOfWork = new UnitOfWork();

        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);

        CompletableFuture<Void> future = jobMapper.findById(jobId)
                .thenApply(ojob -> ojob.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_JOB)))
                .thenCompose(job -> job.getAccount().getForeignObject(unitOfWork))
                .thenApply(account -> {
                    if (!account.getEmail().equals(username)) throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return account;
                })
                .thenCompose(account -> jobExperienceMapper.createAll( jobExperience))
                .thenCompose(aVoid -> unitOfWork.commit());

        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateJob(Job job, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);
        CompletableFuture<Void> future = CompletableFuture.allOf(
                getJob(job.getIdentityKey()),
                job.getAccount().getForeignObject(unitOfWork)
                        .thenApply(account -> {
                            if (!account.getEmail().equals(email))
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                            return account;
                        })
        )
                .thenCompose(aVoid -> jobMapper.update(job))
                .thenCompose(aVoid -> unitOfWork.commit());

        future = handleExceptions(future, unitOfWork);

        return Mono.fromFuture(future);
    }

    public Mono<Void> updateJobExperience(JobExperience jobExperience, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        CompletableFuture<Void> future = getJob(jobExperience.getJobId(), unitOfWork)
                .thenCompose(job -> job.getAccount().getForeignObject(unitOfWork)
                        .thenApply(account -> {
                            if (!account.getEmail().equals(username))
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                            return account;
                        }))
                .thenCompose(account -> jobExperienceMapper.update(jobExperience))
                .thenCompose(aVoid -> unitOfWork.commit());

        future = handleExceptions(future, unitOfWork);

        return Mono.fromFuture(future);
    }

    public Mono<Void> deleteJob(long jobId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Account, Long> accountMapper = getMapper(Account.class, unitOfWork);
        CompletableFuture<Void> future = accountMapper.find(new EqualCondition<String>("email", email))
                .thenApply(accounts -> {
                    if (accounts.isEmpty())
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                    return accounts.get(0);
                })
                .thenCompose(account -> deleteJobAux(unitOfWork, jobId, account))
                .thenCompose(res -> unitOfWork.commit());

        future = handleExceptions(future, unitOfWork);

        return Mono.fromFuture(future);
    }
    //todo commit
    private CompletableFuture<Void> deleteJobAux(UnitOfWork unitOfWork, long jobId, Account account) {
        CompletableFuture<Void> future = getJob(jobId)
                .thenCompose(
                        job -> job.getAccount()
                                .getForeignObject(unitOfWork)
                                .thenCompose(acc -> {
                                    if (!acc.getIdentityKey().equals(account.getIdentityKey()))
                                        throw new BadRequestException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                                    return executeDeleteTransaction(job);
                                })
                );
        return handleExceptions(future, unitOfWork);
    }

    private CompletableFuture<Void> executeDeleteTransaction(Job job) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unitOfWork);
        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        DataMapper<Job, Long> jobeMapper = getMapper(Job.class, unitOfWork);
        CompletableFuture<Void> future = job.getApplications().apply(unitOfWork)
                .thenCompose(applications -> {
                    List<Long> applicationIds = applications.stream().map(Application::getIdentityKey).collect(Collectors.toList());
                    return applicationMapper.deleteAll(applicationIds);
                })
                .thenCompose(aVoid ->
                        job.getExperiences()
                                .apply(unitOfWork)
                                .thenCompose(jobExperiences -> {
                                    List<Long> jobExpIds = jobExperiences.stream().map(JobExperience::getIdentityKey).collect(Collectors.toList());
                                    return jobExperienceMapper.deleteAll(jobExpIds);
                                })
                )
                .thenAccept(aVoid -> jobeMapper.delete( job))
                .thenCompose(aVoid -> unitOfWork.commit());
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> deleteJobExperience(long jxpId, long jobId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        CompletableFuture<Void> future = getJob(jobId)
                .thenCompose(job -> job.getAccount().getForeignObject(unitOfWork)
                        .thenApply(account -> {
                            if (!account.getEmail().equals(email))
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                            return account;
                        }))
                .thenCompose(account -> jobExperienceMapper.deleteById(jxpId))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
