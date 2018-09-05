package isel.ps.employbox.model.binders;

import isel.ps.employbox.model.entities.Company;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.OutCompany;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CompanyBinder implements ModelBinder<Company,OutCompany,InCompany> {

    private final PasswordEncoder passwordEncoder;

    public CompanyBinder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CompletableFuture<OutCompany> bindOutput(Company company) {
        return CompletableFuture.completedFuture(
                new OutCompany(
                        company.getIdentityKey(),
                        company.getEmail(),
                        company.getRating(),
                        company.getName(),
                        company.getSpecialization(),
                        company.getYearFounded(),
                        company.getLogoUrl(),
                        company.getWebPageUrl(),
                        company.getDescription()
                )
        );
    }

    @Override
    public Company bindInput(InCompany obj) {
        return new Company(
                obj.getAccountId(),
                obj.getEmail(),
                obj.getPassword() != null ? passwordEncoder.encode(obj.getPassword()) : null,
                obj.getRating(),
                obj.getName(),
                obj.getSpecialization(),
                obj.getYearFounded(),
                obj.getLogoUrl(),
                obj.getWebpageUrl(),
                obj.getDescription(),
                obj.getCompanyVersion(),
                obj.getAccountVersion()
        );
    }
}
