package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Company;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.OutCompany;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class CompanyBinder implements ModelBinder<Company,OutCompany,InCompany> {

    @Override
    public Mono<OutCompany> bindOutput(CompletableFuture<Company> companyCompletableFuture) {
        return Mono.fromFuture(
                companyCompletableFuture.thenApply(
                        company -> new OutCompany(
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
                )
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
                obj.getDescription(),
                obj.getCompanyVersion(),
                obj.getAccountVersion()
        );
    }
}
