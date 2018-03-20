package isel.ps.EmployBox.api.controllers;

import isel.ps.EmployBox.api.model.output.Company;
import isel.ps.EmployBox.api.services.CompanyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CompanyController {
    @GetMapping("/account/company")
    public List<Company> getCompanies(@RequestParam Map<String,String> queryString){
        return CompanyService.getCompanies(queryString);
    }

    @GetMapping("/account/company/{cid}")
    public List<Company> getCompany(@PathVariable long cid){
        return CompanyService.getCompany(cid);
    }

    @PostMapping("/account/company/")
    public void setCompany(@RequestBody isel.ps.EmployBox.api.model.input.Company company){
        CompanyService.setCompany(company);
    }

    @PutMapping("/account/company/")
    public void updateCompany(@RequestBody isel.ps.EmployBox.api.model.input.Company company){
        CompanyService.updateCompany(company);
    }

    @DeleteMapping("/account/company/{cid}")
    public void deleteCompany(@PathVariable long cid){
        CompanyService.deleteCompany(cid);
    }

}
