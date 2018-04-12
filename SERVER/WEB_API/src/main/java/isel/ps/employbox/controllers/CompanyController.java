package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CompanyBinder;
import isel.ps.employbox.model.input.InCompany;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutCompany;
import isel.ps.employbox.services.CompanyService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

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
    public HalCollection getCompanies(){
        return companyBinder.bindOutput(
                companyService.getCompanies(),
                this.getClass()
        );
    }

    @GetMapping("/{cid}")
    public OutCompany getCompany(@PathVariable long cid){
        return companyBinder.bindOutput( companyService.getCompany(cid));

    }

    @PostMapping
    public void createCompany(
            @RequestBody InCompany inCompany,
            Authentication authentication
    ){
        companyService.createCompany(
                companyBinder.bindInput(inCompany),
                authentication.getName()
        );
    }

    @PutMapping("/{id}")
    public void updateCompany(
            @PathVariable long id,
            @RequestBody InCompany inCompany,
            Authentication authentication
    ){
        if(id != inCompany.getAccountId()) throw new BadRequestException(badRequest_IdsMismatch);
        companyService.updateCompany(
                companyBinder.bindInput(inCompany),
                authentication.getName()

        );
    }

    @DeleteMapping("/{cid}")
    public void deleteCompany(
            @PathVariable long cid,
            Authentication authentication
    ){
        companyService.deleteCompany(cid, authentication.getName());
    }
}
