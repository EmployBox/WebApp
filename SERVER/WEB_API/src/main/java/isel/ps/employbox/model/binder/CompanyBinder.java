package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Company;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.OutCompany;
import org.springframework.stereotype.Component;

@Component
public class CompanyBinder extends ModelBinder<Company, OutCompany, InCompany> {

    @Override
    public OutCompany bindOutput(Company object) {
        return new OutCompany(
                        object.getIdentityKey(),
                        object.getEmail(),
                        object.getRating(),
                        object.getName(),
                        object.getSpecialization(),
                        object.getYearFounded(),
                        object.getLogoUrl(),
                        object.getWebPageUrl(),
                        object.getDescription()
        );
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
