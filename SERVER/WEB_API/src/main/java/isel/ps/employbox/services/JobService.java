package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.mapper.conditions.LikeCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.jobs.Application;
import isel.ps.employbox.model.entities.jobs.Job;
import isel.ps.employbox.model.entities.jobs.JobExperience;
import isel.ps.employbox.model.entities.jobs.Schedule;
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

    public CompletableFuture<CollectionPage<Job>> getAllJobs(
            int page,
            int pageSize,
            String address,
            String title,
            Integer wage,
            String offerType,
            Integer ratingLow,
            Integer ratingHigh,
            String orderColumn,
            String orderClause)
    {
        List<Condition> pairs = new ArrayList<>();
        pairs.add(new LikeCondition("address", address));
        pairs.add(new LikeCondition("title", title));
        pairs.add(new EqualAndCondition<>("wage", wage));
        pairs.add(new EqualAndCondition<>("offerType", offerType));
        pairs.add(new Condition<>("ratingLow",">=", ratingLow));
        pairs.add(new Condition<>("ratingHigh","=<", ratingHigh));


        pairs = pairs.stream()
                .filter(stringPair -> stringPair.getValue() != null)
                .collect(Collectors.toList());

        ServiceUtils.evaluateOrderClause(orderColumn, orderClause, pairs);

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
        pairs.add(new EqualAndCondition<>("jobId", jid));
        Condition[] query = pairs.stream()
                .filter(stringStringPair -> stringStringPair.getValue() != null)
                .toArray(Condition[]::new);
        return ServiceUtils.getCollectionPageFuture(JobExperience.class, page, pageSize, query);
    }

    public CompletableFuture<JobExperience> getJobExperience(long id, long cid) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);


        CompletableFuture<JobExperience> future = jobExperienceMapper.findById( cid)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(oJobExperience -> oJobExperience.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND)))
                .thenCompose(jobExperience ->
                    jobExperience.getJob().getForeignObject(unitOfWork)
                            .thenApply( job-> {
                    if (job.getIdentityKey() != id)
                        throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                    return jobExperience;
                })).thenCompose( res ->unitOfWork.commit().thenApply(__ -> res));

        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<Job> createJob(Job job, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);
        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        DataMapper<Application, Long> jobApplicationMapper = getMapper(Application.class, unitOfWork);
        DataMapper<Schedule, Long> scheduleMapper = getMapper(Schedule.class, unitOfWork);

        CompletableFuture<Job> future = job.getAccount().getForeignObject(unitOfWork)
                .thenApply(account -> {
                    if (account.getEmail().compareTo(email) != 0)
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return account;
                })
                .thenCompose(account -> jobMapper.create( job))
                .thenCompose(aVoid -> job.getApplications().apply(unitOfWork))
                .thenCompose(applicationList -> jobApplicationMapper.createAll(applicationList))
                .thenCompose(aVoid -> job.getExperiences().apply(unitOfWork))
                .thenCompose(experienceList ->  jobExperienceMapper.createAll(experienceList))
                .thenCompose(aVoid -> job.getSchedules().apply(unitOfWork))
                .thenAccept(scheduleList -> scheduleMapper.createAll(scheduleList))
                .thenCompose(__ -> unitOfWork.commit().thenApply(aVoid -> job));
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
        CompletableFuture<Void> future = jobExperience.getJob()
                .getForeignObject(unitOfWork)
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


    public Mono<Void> deleteJob(long jobId, String email, UnitOfWork... unit) {
        UnitOfWork unitOfWork;
        if(unit.length == 0)
            unitOfWork = new UnitOfWork();
        else
            unitOfWork = unit[0];

        AccountService accountService = new AccountService();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unitOfWork);
        DataMapper<Schedule, Long> scheduleMapper = getMapper(Schedule.class, unitOfWork);
        DataMapper<JobExperience, Long> jobExperienceMapper = getMapper(JobExperience.class, unitOfWork);
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unitOfWork);

        CompletableFuture<Void> future = accountService.getAccount(email)
                .thenCompose(acc -> jobMapper.find(new EqualAndCondition<>("jobId", jobId), new EqualAndCondition<>("accountId", acc.getIdentityKey())))
                .thenApply(jobRes -> {
                    if(jobRes.size() == 0)
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_JOB);
                    return jobRes.get(0);
                })
                .thenCompose(job ->
                        job.getSchedules().apply(unitOfWork)
                                .thenApply(schedules -> schedules.stream().map(Schedule::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(scheduleMapper::deleteAll)
                                .thenApply(aVoid -> job)
                )
                .thenCompose(job ->
                        job.getExperiences().apply(unitOfWork)
                                .thenApply(curriculumExperiences -> curriculumExperiences.stream().map(JobExperience::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(jobExperienceMapper::deleteAll)
                                .thenApply(aVoid -> job)
                ).thenCompose(job ->
                        job.getApplications().apply(unitOfWork)
                                .thenApply(jobApplications -> jobApplications.stream().map(Application::getIdentityKey).collect(Collectors.toList()))
                                .thenCompose(applicationMapper::deleteAll)
                                .thenApply(aVoid -> job)
                )
                .thenAccept(job -> jobMapper.deleteById(job.getIdentityKey()))
                .thenCompose(aVoid -> unitOfWork.commit());


        return Mono.fromFuture(future);
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

    public Mono<Void> deleteJobApplication(long jappId, long jobId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Application, Long> jAppMapper = getMapper(Application.class, unitOfWork);
        CompletableFuture<Void> future = getJob(jobId)
                .thenCompose(job -> job.getAccount().getForeignObject(unitOfWork)
                        .thenApply(account -> {
                            if (!account.getEmail().equals(email))
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                            return account;
                        }))
                .thenCompose(account -> jAppMapper.deleteById(jappId))
                .thenCompose(aVoid -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
