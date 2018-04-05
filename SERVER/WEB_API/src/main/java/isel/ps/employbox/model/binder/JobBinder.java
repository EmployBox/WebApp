package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class JobBinder extends ModelBinder<Job, OutJob, InJob> {
    private final ModelBinder<JobExperience, OutJob.OutExperience, InJob.InExperience> experienceBinder;

    public JobBinder(ModelBinder<JobExperience, OutJob.OutExperience, InJob.InExperience> experienceBinder){
        this.experienceBinder = experienceBinder;
    }

    @Override
    public Stream<Job> bindInput(Stream<InJob> list) {
        return list.map(this::bindInput);
    }

    @Override
    public Resource<OutJob> bindOutput(Job job) {
        return new OutJob(job.getAccountID(), job.getTitle(), experienceBinder.bindOutput(job.getExperiences()), job.getAddress(), job.getWage(), job.getDescription(),
                job.getSchedule(), job.getOfferBeginDate(), job.getOfferEndDate(), job.getOfferType());
    }

    @Override
    public Job bindInput(InJob curr) {
        return new Job(curr.getAccountID(), curr.getJobID(), curr.getTitle(), curr.getAddress(), curr.getWage(), curr.getDescription(), curr.getSchedule(), curr.getOfferBeginDate(),
                curr.getOfferEndDate(), curr.getOfferType());
    }
}
