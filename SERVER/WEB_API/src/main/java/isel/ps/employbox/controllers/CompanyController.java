package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CompanyBinder;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutCompany;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.CompanyService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

@RestController
@RequestMapping("/accounts/companies")
public class CompanyController {
    private final CompanyBinder companyBinder;
    private final CompanyService companyService;
    private final APIService apiService;

    public CompanyController(CompanyBinder companyBinder,CompanyService companyService, APIService apiservice){
        this.companyBinder = companyBinder;
        this.companyService = companyService;
        this.apiService = apiservice;
    }

    @GetMapping
    public Resource<HalCollection> getCompanies(){
        return companyBinder.bindOutput(
                companyService.getCompanies(),
                this.getClass()
        );
    }

    @GetMapping("/{cid}")
    public Resource<OutCompany> getCompany(@PathVariable long cid){
        return companyBinder.bindOutput( companyService.getCompany(cid));

    }

    @PostMapping
    public void setCompany(
            @RequestBody InCompany inCompany,
            @RequestHeader("apiKey") String apiKey){
        apiService.validateAPIKey(apiKey);
        companyService.setCompany(
                companyBinder.bindInput(inCompany)
        );
    }

    @PutMapping("/{id}")
    public void updateCompany(
            @PathVariable long id,
            @RequestBody InCompany inCompany
    ){
        if(id != inCompany.getAccountId()) throw new BadRequestException(badRequest_IdsMismatch);
        companyService.updateCompany(
                companyBinder.bindInput(inCompany)
        );
    }

    @DeleteMapping("/{cid}")
    public void deleteCompany(
            @PathVariable long cid){
        companyService.deleteCompany(cid);
    }
}
