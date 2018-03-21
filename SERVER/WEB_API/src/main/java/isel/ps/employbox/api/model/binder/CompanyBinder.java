package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.input.InCompany;
import isel.ps.employbox.api.model.output.OutCompany;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.dal.model.Company;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyBinder implements ModelBinder<Company,OutCompany,InCompany,Long> {
    @Override
    public List<OutCompany> bindOutput(List<Company> list) {
        return null;
    }

    @Override
    public List<Company> bindInput(List<InCompany> list) {
        return null;
    }

    @Override
    public OutCompany bindOutput(Company object) {
        return null;
    }

    @Override
    public Company bindInput(InCompany object) {
        return null;
    }
}
