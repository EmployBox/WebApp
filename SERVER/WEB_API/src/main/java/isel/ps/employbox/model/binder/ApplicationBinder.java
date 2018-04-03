package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.output.OutApplication;
import isel.ps.employbox.model.ModelBinder;
import isel.ps.employbox.model.entities.Application;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationBinder implements ModelBinder<Application, OutApplication, InApplication, String> {
    @Override
    public List<OutApplication> bindOutput(List<Application> list) {
        return list
                .stream()
                .map( (curr)-> new OutApplication (curr.getUserId(), curr.getJobId(), curr.getCurriculumId(), curr.getDate() ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Application> bindInput(List<InApplication> list) {
        return list
                .stream()
                .map((curr-> new Application( curr.getUserId(), curr.getJobId(), curr.getCurriculumId(), curr.getDate() )))
                .collect(Collectors.toList());
    }

    @Override
    public OutApplication bindOutput(Application obj) {
        return new OutApplication (obj.getUserId(), obj.getJobId(), obj.getCurriculumId(), obj.getDate());
    }

    @Override
    public Application bindInput(InApplication obj) {
        return new Application (obj.getUserId(), obj.getJobId(), obj.getCurriculumId(), obj.getDate());
    }
}
