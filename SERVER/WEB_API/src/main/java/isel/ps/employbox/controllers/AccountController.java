package isel.ps.employbox.controllers;

import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutAccount;
import isel.ps.employbox.services.AccountService;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public Mono<HalCollectionPage<Account>> getAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ){
        return Mono.fromFuture( accountService.getAllAccounts(page, pageSize).thenCompose( res -> accountBinder.bindOutput(res, AccountController.class)) );
    }

    @GetMapping("/accId")
    public Mono<OutAccount> getAccount(
            @PathVariable int accId
    ){
        return Mono.fromFuture( accountService.getAccount(accId).thenCompose( res -> accountBinder.bindOutput(res)) );
    }
}
