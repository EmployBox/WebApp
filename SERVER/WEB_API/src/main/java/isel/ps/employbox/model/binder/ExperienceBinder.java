package isel.ps.employbox.model.binder;


import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.hateoas.Resource;

import java.util.stream.Stream;

//todo
public class ExperienceBinder extends ModelBinder<JobExperience,OutJob.OutExperience,InJob.InExperience> {


    @Override
    public Stream<JobExperience> bindInput(Stream<InJob.InExperience> list) {
        return null;
    }

    @Override
    public Resource<OutJob.OutExperience> bindOutput(JobExperience object) {
        return null;
    }

    @Override
    public JobExperience bindInput(InJob.InExperience object) {
        return null;
    }
}
