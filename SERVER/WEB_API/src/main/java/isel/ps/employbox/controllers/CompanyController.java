package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.CompanyBinder;
import isel.ps.employbox.model.entities.Company;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutCompany;
import isel.ps.employbox.services.CompanyService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

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
    public Mono<HalCollectionPage<Company>> getCompanies(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int pageSize){
        CompletableFuture<HalCollectionPage<Company>> future = companyService.getCompanies(page, pageSize)
                .thenApply(companyCollectionPage -> companyBinder.bindOutput(companyCollectionPage, this.getClass()));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{cid}")
    public Mono<OutCompany> getCompany(@PathVariable long cid){
        CompletableFuture<OutCompany> future = companyService.getCompany(cid).thenApply(companyBinder::bindOutput);
        return Mono.fromFuture(future);

    }

    @PostMapping
    public Mono<OutCompany> createCompany(@RequestBody InCompany inCompany){
        CompletableFuture<OutCompany> future = companyService.createCompany(companyBinder.bindInput(inCompany)).thenApply(companyBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @PutMapping("/{id}")
    public Mono<Void> updateCompany(@PathVariable long id, @RequestBody InCompany inCompany, Authentication authentication){
        if(id != inCompany.getAccountId()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Company company = companyBinder.bindInput(inCompany);
        return companyService.updateCompany(company, authentication.getName());
    }

    @DeleteMapping("/{cid}")
    public Mono<Void> deleteCompany(@PathVariable long cid, Authentication authentication){
        return companyService.deleteCompany(cid, authentication.getName());
    }
}
