package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.binder.AccountBinder;
import isel.ps.employbox.api.model.binder.ChatBinder;
import isel.ps.employbox.api.model.binder.JobBinder;
import isel.ps.employbox.api.model.binder.MessageBinder;
import isel.ps.employbox.api.model.input.InChat;
import isel.ps.employbox.api.model.input.InMessage;
import isel.ps.employbox.api.model.output.*;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.AccountService;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountBinder accountBinder;

    private AccountController(AccountService accountService, AccountBinder accountBinder){
        this.accountService = accountService;
        this.accountBinder = accountBinder;
    }

    @GetMapping("/account")
    public List<OutAccount> getAccounts(){
        return accountBinder.bindOutput(accountService.getAccountsPage());
    }

    @GetMapping("/account/{id}")
    public Optional<OutAccount> getAccount(@PathVariable long id){
        return accountService.getAccount(id)
                .map(accountBinder::bindOutput);
    }
}
