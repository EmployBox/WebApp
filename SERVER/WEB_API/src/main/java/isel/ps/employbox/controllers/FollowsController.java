package isel.ps.employbox.controllers;

import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.services.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts/{id}")
public class FollowsController {

    private final AccountBinder accountBinder;
    private final FollowService followService;

    public FollowsController(AccountBinder accountBinder, FollowService followService) {
        this.accountBinder = accountBinder;
        this.followService = followService;
    }

    @GetMapping("/followers")
    public Mono<HalCollectionPage> getFollowers(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int numberOfItems
    ){
        return accountBinder.bindOutput(
                followService.getAccountFollowers(id, page, numberOfItems),
                this.getClass(),
                id
        );
    }

    @GetMapping("/following")
    public Mono<HalCollectionPage> getFollowing(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int numberOfItems

    ){
        return accountBinder.bindOutput(
                followService.getAccountFollowing(id, page, numberOfItems),
                this.getClass(),
                id
        );
    }

    @PutMapping("/followers/{fid}")
    public Mono<Void> setFollower(@PathVariable long id, @PathVariable long fid,  Authentication authentication){
        return followService.createFollower(id, fid, authentication.getName());
    }

    @DeleteMapping("/followers/{fid}")
    public Mono<Void> deleteFollower(@PathVariable long id, @PathVariable long fid, Authentication authentication){
        return followService.deleteFollower(id, fid, authentication.getName());
    }
}
