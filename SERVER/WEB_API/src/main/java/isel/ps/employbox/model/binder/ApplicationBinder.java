package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.output.OutApplication;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBinder extends ModelBinder<Application, OutApplication, InApplication>{


    public OutApplication bindOutput(Application obj) {
        return new OutApplication (obj.getUserId(), obj.getJobId(), obj.getCurriculumId(), obj.getDate());
    }

    @Override
    public Application bindInput(InApplication obj) {
        return new Application (obj.getUserId(), obj.getJobId(), obj.getCurriculumId(), obj.getDate());
    }
}
