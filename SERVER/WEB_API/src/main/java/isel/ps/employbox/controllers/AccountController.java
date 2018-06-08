package isel.ps.employbox.controllers;

import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.services.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountBinder accountBinder;
    private final AccountService accountService;

    public AccountController(AccountBinder accountBinder, AccountService accountService) {
        this.accountBinder = accountBinder;
        this.accountService = accountService;
    }

    @GetMapping()
    public Mono<CollectionPage<Account>> getAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ){
        return Mono.fromFuture( accountService.getAllAccounts(page, pageSize) );
    }
}
