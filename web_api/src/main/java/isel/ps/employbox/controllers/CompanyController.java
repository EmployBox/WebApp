package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.CompanyBinder;
import isel.ps.employbox.model.entities.Company;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
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
    public Mono<HalCollectionPage<Company>> getCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Integer yearFounded,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer ratingLow,
            @RequestParam(required = false) Integer ratingHigh,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    ) {
        CompletableFuture<HalCollectionPage<Company>> future = companyService.getCompanies(page, pageSize,ratingLow, ratingHigh, name, specialization, yearFounded, location, orderColumn, orderClause)
                .thenCompose(companyCollectionPage -> companyBinder.bindOutput(companyCollectionPage, this.getClass()));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{cid}")
    public Mono<OutCompany> getCompany(@PathVariable long cid){
        CompletableFuture<OutCompany> future = companyService.getCompany(cid)
                .thenCompose(companyBinder::bindOutput);
        return Mono.fromFuture(future);

    }

    @PostMapping
    public Mono<OutCompany> createCompany(@RequestBody InCompany inCompany){
        CompletableFuture<OutCompany> future = companyService.createCompany(companyBinder.bindInput(inCompany))
                .thenCompose(companyBinder::bindOutput);
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
