package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.input.InCompany;
import isel.ps.employbox.api.model.output.OutCompany;
import isel.ps.employbox.api.services.CompanyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CompanyController {
    @GetMapping("/account/company")
    public List<OutCompany> getCompanies(@RequestParam Map<String,String> queryString){
        return CompanyService.getCompanies(queryString);
    }

    @GetMapping("/account/company/{cid}")
    public List<OutCompany> getCompany(@PathVariable long cid){
        return CompanyService.getCompany(cid);
    }

    @PostMapping("/account/company/")
    public void setCompany(@RequestBody InCompany inCompany){
        CompanyService.setCompany(inCompany);
    }

    @PutMapping("/account/company/")
    public void updateCompany(@RequestBody InCompany inCompany){
        CompanyService.updateCompany(inCompany);
    }

    @DeleteMapping("/account/company/{cid}")
    public void deleteCompany(@PathVariable long cid){
        CompanyService.deleteCompany(cid);
    }

}
