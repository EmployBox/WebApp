package isel.ps.employbox.model.binder;


import isel.ps.employbox.model.entities.JobExperience;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import org.springframework.hateoas.Resource;


public class ExperienceBinder extends ModelBinder<JobExperience,OutJob.OutExperience,InJob.InExperience> {


    @Override
    public Resource<OutJob.OutExperience> bindOutput(JobExperience object) {
        return new Resource<>( new OutJob.OutExperience(object.getCompetences(), object.getYears()));
    }

    @Override
    public JobExperience bindInput(InJob.InExperience object) {
        return new JobExperience( object.getYears(), object.getCompetence(),object.getYears());
    }
}
