package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.binder.CompanyBinder;
import isel.ps.employbox.api.model.input.InCompany;
import isel.ps.employbox.api.model.output.OutCompany;
import isel.ps.employbox.api.services.CompanyService;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.dal.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CompanyController {
    @Autowired
    private CompanyBinder companyBinder;

    @GetMapping("/account/company")
    public List<OutCompany> getCompanies(@RequestParam Map<String,String> queryString){
        return companyBinder.bindOutput(
                CompanyService.getCompanies(queryString)
        );
    }

    @GetMapping("/account/company/{cid}")
    public List<OutCompany> getCompany(@PathVariable long cid){
        return companyBinder.bindOutput(
                CompanyService.getCompany(cid)
        );
    }

    @PostMapping("/account/company/")
    public void setCompany(@RequestBody InCompany inCompany){
        CompanyService.setCompany(
                companyBinder.bindInput(inCompany)
        );
    }

    @PutMapping("/account/company/")
    public void updateCompany(@RequestBody InCompany inCompany){
        CompanyService.updateCompany(
                companyBinder.bindInput(inCompany)
        );
    }

    @DeleteMapping("/account/company/{cid}")
    public void deleteCompany(@PathVariable long cid){
        CompanyService.deleteCompany(cid);
    }

}
