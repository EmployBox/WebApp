package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class JobService {
    private final DataRepository<Job, Long> jobRepo;
    private final DataRepository<JobExperience, Long> jobExperienceRepo;
    private final AccountService accountService;

    public JobService(DataRepository<Job, Long> jobRepo, DataRepository<JobExperience, Long> jobExperienceExperienceRepo, AccountService userService) {
        this.jobRepo = jobRepo;
        this.jobExperienceRepo = jobExperienceExperienceRepo;
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

    public Mono<Void> updateJob(Job job, String email) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        getJob(job.getIdentityKey()),
                        accountService.getAccount(job.getAccountId(), email)
                ).thenCompose( __-> jobRepo.update(job))
                 .thenAccept( res -> {
                    if(res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                })
        );
    }

    public CompletableFuture<Job> createJob(Job job, String email) {
        return accountService.getAccount(job.getAccountId(), email)
                .thenCompose(__ -> jobRepo.create(job))
                .thenCompose(res -> {
                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                    return job.getExperiences();
                })
                .thenCompose(
                        experienceList -> {
                            if(experienceList.isEmpty())
                                return CompletableFuture.completedFuture(Optional.empty());
                            experienceList.forEach(curr -> curr.setJobId(job.getIdentityKey()));
                            return jobExperienceRepo.createAll(experienceList);
                        }
                ).thenApply(res -> {
                            if (res.isPresent()) throw new BadRequestException(ErrorMessages.CHILDS_CREATION);
                            return job;
                        }

                );
    }

    public Mono<Void> deleteJob( long id, String email) {
        return Mono.fromFuture(
           accountService.getAccount(id, email)
                   .thenAccept(user -> getJob(id).thenCompose( job -> {
                           if(job.getAccountId() != user.getIdentityKey())
                               throw new BadRequestException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                            return jobRepo.delete(job);
                       }).thenAccept( res -> {
                           if(res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                       })
                   )
       );
    }

    public CompletableFuture<Void> addJobExperienceToJob(long jobId, List<JobExperience> jobExperience, String username){
        return getJob(jobId)
                .thenCompose(job -> accountService.getAccount(job.getAccountId(), username))
                .thenCompose( __ -> jobExperienceRepo.createAll(jobExperience))
                .thenAccept( res -> {
                   if(res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                });
    }

    public Mono<Void> updateJobExperience(long jxpId, long jobId, JobExperience jobExperience, String username) {
        if(jobId != jobExperience.getJobId() && jxpId != jobExperience.getIdentityKey())
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        return Mono.fromFuture(
                getJob(jobExperience.getJobId())
                        .thenCompose(job ->
                                accountService.getAccount(job.getAccountId(), username))
                        .thenCompose(account ->
                                jobExperienceRepo.update(jobExperience))
                        .thenAccept(res -> {
                                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                                }
                        )
        );
    }

    public Mono<Void> deleteJobExperience(long jxpId, long jobId, String email) {
        return Mono.fromFuture(
                getJob(jobId)
                        .thenCompose( job-> accountService.getAccount(job.getAccountId(), email))
                        .thenCompose( __ -> jobExperienceRepo.deleteById(jxpId))
                        .thenAccept(res -> { if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION); })
        );
    }
}
