package isel.ps.employbox.controllers.account.FollowsControllers;

import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.services.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts/{accountId}/followed")
public class FollowedController {
    private final AccountBinder accountBinder;
    private final FollowService followService;

    public FollowedController(AccountBinder accountBinder, FollowService followService) {
        this.accountBinder = accountBinder;
        this.followService = followService;
    }

    @GetMapping
    public Mono<HalCollectionPage<Account>> getTheAccountsWichThisAccountIsFollowed(
            @PathVariable long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause,
            @RequestParam(required = false) Long accountToCheck

    ){
        CompletableFuture<CollectionPage<Account>> future;
        if(accountToCheck==null)
            future = followService.getAccountFolloweds(accountId, page, pageSize, orderColumn, orderClause);
        else
            future = followService.checkIfAccountIsFollowing(accountId, accountToCheck);

        return Mono.fromFuture(future
                .thenCompose(accountCollectionPage -> accountBinder.bindOutput(accountCollectionPage, this.getClass(), accountId))
        );
    }

    @PutMapping
    public Mono<Void> addNewFollowRelation(
            @PathVariable long accountId,
            Authentication authentication)
    {
        return followService.followNewAccount(accountId, authentication.getName());
    }

    @DeleteMapping
    public Mono<Void> deleteAFollowRelation(
            @PathVariable long accountId,
            Authentication authentication)
    {
        return followService.deleteFollower(accountId, authentication.getName());
    }
}
