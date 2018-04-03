package isel.ps.employbox.controllers;

import isel.ps.employbox.model.binder.AccountBinder;
import isel.ps.employbox.services.AccountService;
import isel.ps.employbox.model.output.OutAccount;
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
