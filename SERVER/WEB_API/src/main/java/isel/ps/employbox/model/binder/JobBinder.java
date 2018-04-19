package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class JobBinder extends ModelBinder<Job, OutJob, InJob> {
    private final ExperienceBinder experienceBinder;

    public JobBinder(ExperienceBinder experienceBinder){
        this.experienceBinder = experienceBinder;
    }

    @Override
    public OutJob bindOutput(Job job) {
        return new OutJob(
                job.getAccountID(),
                job.getIdentityKey(),
                job.getTitle(),
                bindExperience(job.getExperiences().get()),
                job.getAddress(),
                job.getWage(),
                job.getDescription(),
                job.getSchedule(),
                job.getOfferBeginDate(),
                job.getOfferEndDate(),
                job.getOfferType());
    }

    @Override
    public Job bindInput(InJob curr) {
        return new Job(curr.getAccountID(), curr.getJobID(), curr.getTitle(), curr.getAddress(), curr.getWage(), curr.getDescription(), curr.getSchedule(), curr.getOfferBeginDate(),
                curr.getOfferEndDate(), curr.getOfferType());
    }

    private List<OutJob.OutExperience> bindExperience(List<JobExperience> list){
        return StreamSupport.stream(list.spliterator(),false)
                .map(curr-> new OutJob.OutExperience(curr.getCompetences(), curr.getYears()))
                .collect(Collectors.toList());
    }
}
