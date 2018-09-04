package isel.ps.employbox.controllers.account;

import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutAccount;
import isel.ps.employbox.services.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    ){
        return Mono.fromFuture(accountService.getAllAccounts(page, pageSize, orderColumn,orderClause).thenCompose(res -> accountBinder.bindOutput(res, AccountController.class)) );
    }


    @GetMapping("/self")
    public Mono<OutAccount> getAccount(Authentication authentication){
        Account principal = (Account) authentication.getPrincipal();
        CompletableFuture<OutAccount> future = accountService.getAccount(principal.getEmail())
                .thenCompose(accountBinder::bindOutput);
        return Mono.fromFuture(future);
    }
}
