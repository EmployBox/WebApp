package isel.ps.employbox.api.model.binder;


import isel.ps.employbox.api.model.input.InJob;
import isel.ps.employbox.api.model.output.OutJob;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.dal.model.JobExperience;

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
