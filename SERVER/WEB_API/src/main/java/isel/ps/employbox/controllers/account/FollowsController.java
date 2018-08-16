package isel.ps.employbox.controllers.account;

import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.services.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts/{accountId}")
public class FollowsController {

    private final AccountBinder accountBinder;
    private final FollowService followService;

    public FollowsController(AccountBinder accountBinder, FollowService followService) {
        this.accountBinder = accountBinder;
        this.followService = followService;
    }

    @GetMapping("/followers")
    public Mono<HalCollectionPage<Account>> getFollowers(
            @PathVariable long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    ){
        CompletableFuture<HalCollectionPage<Account>> future = followService.getAccountFollowers(accountId, page, pageSize, orderColumn, orderClause)
                .thenCompose(accountCollectionPage -> accountBinder.bindOutput(accountCollectionPage, this.getClass(), accountId));
        return Mono.fromFuture(future);
    }

    @GetMapping("/following")
    public Mono<HalCollectionPage<Account>> getFollowing(
            @PathVariable long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause

    ){
        CompletableFuture<HalCollectionPage<Account>> future = followService.getAccountFolloweds(accountId, page, pageSize, orderColumn, orderClause)
                .thenCompose(accountCollectionPage -> accountBinder.bindOutput(accountCollectionPage, this.getClass(), accountId));
        return Mono.fromFuture(future);
    }

    @PutMapping("/followers")
    public Mono<Void> setFollower(
            @PathVariable long accountId,
            @RequestParam long accountToBeFollowedId,
            Authentication authentication)
    {
        return followService.createFollower(accountId, accountToBeFollowedId, authentication.getName());
    }

    @DeleteMapping("/followers")
    public Mono<Void> deleteFollower(
            @PathVariable long id,
            @RequestParam long accountToBeFollowedId,
            Authentication authentication)
    {
        return followService.deleteFollower(id, accountToBeFollowedId, authentication.getName());
    }
}
