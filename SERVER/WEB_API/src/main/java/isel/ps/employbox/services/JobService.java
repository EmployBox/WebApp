package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class JobService {
    private final Logger log = LoggerFactory.getLogger(JobService.class);
    private final DataRepository<Job, Long> jobRepo;
    private final DataRepository<JobExperience, JobExperience.JobExperienceKey> jobExperienceExperienceRepo;
    private final UserService userService;

    public JobService(DataRepository<Job, Long> jobRepo, DataRepository<JobExperience, JobExperience.JobExperienceKey> jobExperienceExperienceRepo, UserService userService) {
        this.jobRepo = jobRepo;
        this.jobExperienceExperienceRepo = jobExperienceExperienceRepo;
        this.userService = userService;
    }

    public CompletableFuture<Stream<Job> > getAllJobs() {
        return  jobRepo.findAll().thenApply( list -> list.stream());
    }

    public CompletableFuture<Job> getJob(long jid) {
        return jobRepo.findById(jid)
                .thenApply(ojob -> ojob.orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.resourceNotfound_job)));
    }

    public Mono<Void> updateJob(Job job, String email) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        getJob(job.getIdentityKey()),
                        userService.getUser(job.getAccountId(), email)
                ).thenCompose( __-> jobRepo.update(job))
                 .thenAccept( res -> {
                    if(!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                })
        );
    }

    public CompletableFuture<Job> createJob(Job job, String email) {
        return userService.getUser(job.getAccountId(), email)
                .thenCompose(__ -> jobRepo.create(job))
                .thenCompose(res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);

                    return jobExperienceExperienceRepo.createAll(job.getExperiences().join());
                }).thenApply(res -> {
                            if (!res) log.info(ErrorMessages.jobExperience_ItemCreation);
                            return job;
                        }
                );
    }

    public Mono<Void> deleteJob(long id, String email) {
       return Mono.fromFuture(
           userService.getUser(id, email)
                   .thenAccept(user -> getJob(id).thenCompose( job -> {
                           if(job.getAccountId() != user.getIdentityKey())
                               throw new BadRequestException(ErrorMessages.unAuthorized_IdAndEmailMismatch);
                            return jobRepo.delete(job);
                       }).thenAccept( res -> {
                           if(!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion );
                       })
                   )
       );
    }
}
