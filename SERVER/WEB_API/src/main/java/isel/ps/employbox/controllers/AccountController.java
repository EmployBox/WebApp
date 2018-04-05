package isel.ps.employbox.controllers;

import isel.ps.employbox.model.binder.AccountBinder;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutAccount;
import isel.ps.employbox.services.AccountService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountBinder accountBinder;

    private AccountController(AccountService accountService, AccountBinder accountBinder){
        this.accountService = accountService;
        this.accountBinder = accountBinder;
    }

    @GetMapping
    public Resource<HalCollection> getAccounts(){
        return accountBinder.bindOutput(
                accountService.getAllAccounts(),
                this.getClass()
        );
    }

    @GetMapping("/{id}")
    public Resource<OutAccount> getAccount(@PathVariable long id){
        return accountBinder.bindOutput( accountService.getAccount(id));
    }
}
