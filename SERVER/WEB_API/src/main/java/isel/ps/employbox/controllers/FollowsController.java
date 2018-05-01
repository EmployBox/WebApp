package isel.ps.employbox.controllers;

import isel.ps.employbox.model.binder.AccountBinder;
import isel.ps.employbox.model.output.HalCollection;
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
    public Mono<HalCollection> getFollowers(@PathVariable long id){
        return accountBinder.bindOutput(
                followService.getAccountFollowers(id),
                this.getClass()
        );
    }

    @GetMapping("/following")
    public Mono<HalCollection> getFollowing(@PathVariable long id){
        return accountBinder.bindOutput(
                followService.getAccountFollowing(id),
                this.getClass()
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
