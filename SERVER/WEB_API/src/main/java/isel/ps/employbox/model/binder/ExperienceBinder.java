package isel.ps.employbox.model.binder;


import isel.ps.employbox.model.ModelBinder;
import isel.ps.employbox.model.input.InJob;
import isel.ps.employbox.model.output.OutJob;
import isel.ps.employbox.model.entities.JobExperience;

import java.util.List;

public class ExperienceBinder implements ModelBinder<JobExperience,OutJob.OutExperience,InJob.InExperience,Long> {


    @Override
    public List<OutJob.OutExperience> bindOutput(List<JobExperience> list) {
        return null;
    }

    @Override
    public List<JobExperience> bindInput(List<InJob.InExperience> list) {
        return null;
    }

    @Override
    public OutJob.OutExperience bindOutput(JobExperience object) {
        return null;
    }

    @Override
    public JobExperience bindInput(InJob.InExperience object) {
        return null;
    }
}
