package isel.ps.employbox.controllers;

import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutAccount;
import isel.ps.employbox.services.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts")
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
        return Mono.fromFuture(accountService.getAllAccounts(page, pageSize).thenCompose(res -> accountBinder.bindOutput(res, AccountController.class)) );
    }

    @GetMapping("/self")
    public Mono<OutAccount> getAccount(Authentication authentication){
        Account principal = (Account) authentication.getPrincipal();
        CompletableFuture<OutAccount> future = accountService.getAccount(principal.getEmail())
                .thenCompose(accountBinder::bindOutput);
        return Mono.fromFuture(future);
    }
}
