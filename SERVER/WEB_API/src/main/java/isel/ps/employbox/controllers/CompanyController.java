package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.CompanyBinder;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutCompany;
import isel.ps.employbox.services.CompanyService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/companies")
public class CompanyController {
    private final CompanyBinder companyBinder;
    private final CompanyService companyService;

    public CompanyController(CompanyBinder companyBinder,CompanyService companyService){
        this.companyBinder = companyBinder;
        this.companyService = companyService;
    }

    @GetMapping
    public Mono<HalCollectionPage> getCompanies( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int numberOfItems){
        return companyBinder.bindOutput(
                companyService.getCompanies(page, numberOfItems),
                this.getClass()
        );
    }

    @GetMapping("/{cid}")
    public Mono<OutCompany> getCompany(@PathVariable long cid){
        return companyBinder.bindOutput( companyService.getCompany(cid));

    }

    @PostMapping
    public Mono<OutCompany> createCompany(@RequestBody InCompany inCompany){
        return companyBinder.bindOutput(
                companyService.createCompany(companyBinder.bindInput(inCompany))
        );
    }

    @PutMapping("/{id}")
    public Mono<Void> updateCompany(@PathVariable long id, @RequestBody InCompany inCompany, Authentication authentication){
        if(id != inCompany.getAccountId()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return companyService.updateCompany(
                companyBinder.bindInput(inCompany), authentication.getName()
        );
    }

    @DeleteMapping("/{cid}")
    public Mono<Void> deleteCompany(@PathVariable long cid, Authentication authentication){
        return companyService.deleteCompany(cid, authentication.getName());
    }
}
