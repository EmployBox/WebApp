package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.ErrorMessages;
import isel.ps.employbox.api.exceptions.BadRequestException;
import isel.ps.employbox.api.model.binder.CompanyBinder;
import isel.ps.employbox.api.model.input.InCompany;
import isel.ps.employbox.api.model.output.OutCompany;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.CompanyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static isel.ps.employbox.api.ErrorMessages.*;

@RestController
public class CompanyController {
    private final CompanyBinder companyBinder;
    private final CompanyService companyService;
    private final APIService apiService;

    public CompanyController(CompanyBinder companyBinder,CompanyService companyService, APIService apiservice){
        this.companyBinder = companyBinder;
        this.companyService = companyService;
        this.apiService = apiservice;
    }

    @GetMapping("/account/company")
    public List<OutCompany> getCompanies(){
        return companyBinder.bindOutput(
                companyService.getCompanies()
        );
    }

    @GetMapping("/account/company/{cid}")
    public Optional<OutCompany> getCompany(@PathVariable long cid){
        return companyService.getCompany(cid).map(companyBinder::bindOutput);

    }

    @PostMapping("/account/company/")
    public void setCompany(
            @RequestBody InCompany inCompany,
            @RequestHeader("apiKey") String apiKey){
        apiService.validateAPIKey(apiKey);
        companyService.setCompany(
                companyBinder.bindInput(inCompany)
        );
    }

    @PutMapping("/account/company/{id}")
    public void updateCompany(
            @PathVariable long id,
            @RequestBody InCompany inCompany,
            @RequestHeader("apiKey") String apiKey
    ){
        if(id != inCompany.getAccountId()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        apiService.validateAPIKey(apiKey);
        companyService.updateCompany(
                companyBinder.bindInput(inCompany)
        );
    }

    @DeleteMapping("/account/company/{cid}")
    public void deleteCompany(
            @PathVariable long cid,
            @RequestHeader("apiKey") String apiKey){
        apiService.validateAPIKey(apiKey);
        companyService.deleteCompany(cid);
    }

}
