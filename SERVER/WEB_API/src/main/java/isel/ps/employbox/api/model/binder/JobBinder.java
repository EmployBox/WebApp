package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.input.InJob;
import isel.ps.employbox.api.model.output.OutJob;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.dal.model.Job;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobBinder implements ModelBinder<Job, OutJob, InJob, Long> {
    private final ExperienceBinder experienceBinder;

    public JobBinder(ExperienceBinder experienceBinder){
        this.experienceBinder = experienceBinder;
    }

    @Override
    public List<OutJob> bindOutput(List<Job> list) {
        return list
            .stream()
            .map((curr)-> new OutJob(
                curr.getAccountID(),
                experienceBinder.bindOutput(curr.getExperiences().get()),
                curr.getAddress(),
                curr.getWage(),
                curr.getDescription(),
                curr.getSchedule(),
                curr.getOfferBeginDate(),
                curr.getOfferEndDate(),
                curr.getOfferType()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Job> bindInput(List<InJob> list) {
        return list
            .stream()
            .map((curr)-> new Job(
                curr.getAccountID(),
                curr.getJobID(),
                curr.getAddress(),
                curr.getWage(),
                curr.getDescription(),
                curr.getSchedule(),
                curr.getOfferBeginDate(),
                curr.getOfferEndDate(),
                curr.getOfferType()))
            .collect(Collectors.toList());
    }

    @Override
    public OutJob bindOutput(Job job) {
        return new OutJob(
            job.getAccountID(),
            experienceBinder.bindOutput(job.getExperiences().get()),
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
        return new Job(
            curr.getAccountID(),
            curr.getJobID(),
            curr.getAddress(),
            curr.getWage(),
            curr.getDescription(),
            curr.getSchedule(),
            curr.getOfferBeginDate(),
            curr.getOfferEndDate(),
            curr.getOfferType());
    }
}
