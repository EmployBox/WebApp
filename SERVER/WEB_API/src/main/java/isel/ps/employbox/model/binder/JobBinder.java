package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Job;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class JobBinder extends ModelBinder<Job, OutJob, InJob> {
    private final ExperienceBinder experienceBinder;

    public JobBinder(ExperienceBinder experienceBinder){
        this.experienceBinder = experienceBinder;
    }

    @Override
    public Stream<Job> bindInput(Stream<InJob> list) {
        return list.map(this::bindInput);
    }

    @Override
    public Resource<OutJob> bindOutput(Job job) {
        return new Resource( new OutJob(
                job.getAccountID(),
                job.getJobId(),
                job.getTitle(),
                StreamSupport.stream(job.getExperiences().get().spliterator(),false )
                        .map(curr -> experienceBinder.bindOutput(curr).getContent() ).collect(Collectors.toList()),
                job.getAddress(),
                job.getWage(),
                job.getDescription(),
                job.getSchedule(),
                job.getOfferBeginDate(),
                job.getOfferEndDate(),
                job.getOfferType()));
    }

    @Override
    public Job bindInput(InJob curr) {
        return new Job(curr.getAccountID(), curr.getJobID(), curr.getTitle(), curr.getAddress(), curr.getWage(), curr.getDescription(), curr.getSchedule(), curr.getOfferBeginDate(),
                curr.getOfferEndDate(), curr.getOfferType());
    }
}
