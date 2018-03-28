package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.binder.AccountBinder;
import isel.ps.employbox.api.model.output.*;
import isel.ps.employbox.api.services.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public List<OutAccount> getAccounts(){
        return accountBinder.bindOutput(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public Optional<OutAccount> getAccount(@PathVariable long id){
        return accountService.getAccount(id)
                .map(accountBinder::bindOutput);
    }
}
