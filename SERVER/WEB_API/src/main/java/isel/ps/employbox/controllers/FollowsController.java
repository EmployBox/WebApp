package isel.ps.employbox.controllers;

import isel.ps.employbox.model.binder.AccountBinder;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.services.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public HalCollection getFollowers(@PathVariable long id){
        return accountBinder.bindOutput(
                followService.getAccountFollowers(id),
                this.getClass(),
                id
        );
    }

    @GetMapping("/following")
    public HalCollection getFollowing(@PathVariable long id,  Authentication authentication){
        return accountBinder.bindOutput(
                followService.getAccountFollowing(id, authentication.getName()),
                this.getClass(),
                id
        );
    }

    @PutMapping("/followers/{fid}")
    public void setFollower(@PathVariable long id, @PathVariable long fid,  Authentication authentication){
        followService.setFollower(id, fid, authentication.getName());
    }

    @DeleteMapping("/followers/{fid}")
    public void deleteFollower(@PathVariable long id, @PathVariable long fid, Authentication authentication){
        followService.deleteFollower(id, fid, authentication.getName());
    }
}
