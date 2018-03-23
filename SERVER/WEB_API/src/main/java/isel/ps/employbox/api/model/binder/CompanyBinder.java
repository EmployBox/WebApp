package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.input.InCompany;
import isel.ps.employbox.api.model.output.OutCompany;
import isel.ps.employbox.api.model.ModelBinder;
import isel.ps.employbox.dal.model.Company;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyBinder implements ModelBinder<Company, OutCompany, InCompany, Long> {
    @Override
    public List<OutCompany> bindOutput(List<Company> list) {
        return null;
    }

    @Override
    public List<Company> bindInput(List<InCompany> list) {
        return list.stream()
                .map(curr -> new Company(
                        curr.getAccountId(),
                        curr.getEmail(),
                        curr.getPassword(),
                        curr.getRating(),
                        curr.getName(),
                        curr.getSpecialization(),
                        curr.getYearFounded(),
                        curr.getLogoUrl(),
                        curr.getWebpageUrl(),
                        curr.getDescription()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public OutCompany bindOutput(Company object) {
        return null;
    }

    @Override
    public Company bindInput(InCompany obj) {

        return new Company(
                obj.getAccountId(),
                obj.getEmail(),
                obj.getPassword(),
                obj.getRating(),
                obj.getName(),
                obj.getSpecialization(),
                obj.getYearFounded(),
                obj.getLogoUrl(),
                obj.getWebpageUrl(),
                obj.getDescription());
    }
}
