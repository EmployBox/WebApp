package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.input.InApplication;
import isel.ps.employbox.api.model.output.OutApplication;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.dal.model.Application;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationBinder implements ModelBinder<Application, OutApplication, InApplication, String> {
    @Override
    public List<OutApplication> bindOutput(List<Application> list) {
        return null;
    }

    @Override
    public List<Application> bindInput(List<InApplication> list) {
        return null;
    }

    @Override
    public OutApplication bindOutput(Application object) {
        return null;
    }

    @Override
    public Application bindInput(InApplication object) {
        return null;
    }
}
