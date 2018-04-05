package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Company;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.OutCompany;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class CompanyBinder extends ModelBinder<Company, OutCompany, InCompany> {


    @Override
    public Stream<Company> bindInput(Stream<InCompany> list) {
        return list.map(this::bindInput);
    }

    @Override
    public Resource<OutCompany> bindOutput(Company object) {
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
