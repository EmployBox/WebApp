package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class JobBinder implements ModelBinder<Job,OutJob,InJob> {
    private final ExperienceBinder experienceBinder;

    public JobBinder(ExperienceBinder experienceBinder){
        this.experienceBinder = experienceBinder;
    }

    @Override
    public Mono<OutJob> bindOutput(CompletableFuture<Job> jobCompletableFuture) {
        return Mono.fromFuture(
                jobCompletableFuture
                        .thenApply(job-> new OutJob(
                                    job.getAccountId(),
                                    job.getIdentityKey(),
                                    job.getTitle(),
                                    bindExperience(job.getExperiences().get()),
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
    public Job bindInput(InJob curr) {
        return new Job(curr.getAccountId(), curr.getJobID(), curr.getTitle(), curr.getAddress(), curr.getWage(), curr.getDescription(), curr.getSchedule(), curr.getOfferBeginDate(),
                curr.getOfferEndDate(), curr.getOfferType(), curr.getVersion());
    }

    private List<OutJob.OutExperience> bindExperience(List<JobExperience> list){
        return list.stream()
                .map(curr-> new OutJob.OutExperience(curr.getCompetences(), curr.getYears()))
                .collect(Collectors.toList());
    }
}
