package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CompanyBinder;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.OutCompany;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.CompanyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static isel.ps.employbox.ErrorMessages.*;

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
    public List<OutCompany> getCompanies(){
        return companyBinder.bindOutput(
                companyService.getCompanies()
        );
    }

    @GetMapping("/{cid}")
    public Optional<OutCompany> getCompany(@PathVariable long cid){
        return companyService.getCompany(cid).map(companyBinder::bindOutput);

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
            @RequestBody InCompany inCompany,
            @RequestHeader("apiKey") String apiKey
    ){
        if(id != inCompany.getAccountId()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        apiService.validateAPIKey(apiKey);
        companyService.updateCompany(
                companyBinder.bindInput(inCompany)
        );
    }

    @DeleteMapping("/{cid}")
    public void deleteCompany(
            @PathVariable long cid,
            @RequestHeader("apiKey") String apiKey){
        apiService.validateAPIKey(apiKey);
        companyService.deleteCompany(cid);
    }

}
