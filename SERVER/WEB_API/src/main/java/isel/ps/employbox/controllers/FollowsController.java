package isel.ps.employbox.controllers;

import isel.ps.employbox.model.binder.AccountBinder;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.FollowService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts/{id}")
public class FollowsController {

    private final AccountBinder accountBinder;
    private final FollowService followService;
    private final APIService apiService;

    public FollowsController(AccountBinder accountBinder, FollowService followService, APIService apiService) {
        this.accountBinder = accountBinder;
        this.followService = followService;
        this.apiService = apiService;
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
    public HalCollection getFollowing(@PathVariable long id){
        return accountBinder.bindOutput(
                followService.getAccountFollowing(id),
                this.getClass(),
                id
        );
    }

    @PutMapping("/followers/{fid}")
    public void setFollower(@PathVariable long id, @PathVariable long fid){
        followService.setFollower(id,fid);
    }

    @DeleteMapping("/followers/{fid}")
    public void deleteFollower(@PathVariable long id, @PathVariable long fid){
        followService.deleteFollower(id,fid);
    }
}
