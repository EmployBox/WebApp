package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.output.OutApplication;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class ApplicationBinder extends ModelBinder<Application, OutApplication, InApplication>{


    public Resource<OutApplication> bindOutput(Application obj) {
        return new Resource( new OutApplication (obj.getUserId(), obj.getJobId(), obj.getCurriculumId(), obj.getDate()));
    }

    @Override
    public Stream<Application> bindInput(Stream<InApplication> list) {
        return null;
    }

    @Override
    public Application bindInput(InApplication obj) {
        return new Application (obj.getUserId(), obj.getJobId(), obj.getCurriculumId(), obj.getDate());
    }
}
