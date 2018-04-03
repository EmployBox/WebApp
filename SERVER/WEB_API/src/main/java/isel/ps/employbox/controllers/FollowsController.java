package isel.ps.employbox.controllers;

import isel.ps.employbox.model.ModelBinder;
import isel.ps.employbox.model.input.InAccount;
import isel.ps.employbox.model.output.OutAccount;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.FollowService;
import isel.ps.employbox.model.entities.Account;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts/{id}")
public class FollowsController {

    private final ModelBinder<Account, OutAccount, InAccount, Long> accountBinder;
    private final FollowService followService;
    private final APIService apiService;

    public FollowsController(ModelBinder<Account, OutAccount, InAccount, Long> accountBinder, FollowService followService, APIService apiService) {
        this.accountBinder = accountBinder;
        this.followService = followService;
        this.apiService = apiService;
    }

    @GetMapping("/followers")
    public List<OutAccount> getFollowers(@PathVariable long id){
        return accountBinder.bindOutput(followService.getAccountFollowers(id));
    }

    @GetMapping("/following")
    public List<OutAccount> getFollowing(@PathVariable long id){
        return accountBinder.bindOutput(followService.getAccountFollowing(id));
    }

    @PutMapping("/followers/{fid}")
    public void setFollower(@PathVariable long id, @PathVariable long fid, @RequestHeader("apiKey") String apiKey){
        apiService.validateAPIKey(apiKey);
        followService.setFollower(id,fid);
    }

    @DeleteMapping("/followers/{fid}")
    public void deleteFollower(@PathVariable long id, @PathVariable long fid, @RequestHeader("apiKey") String apiKey){
        apiService.validateAPIKey(apiKey);
        followService.deleteFollower(id,fid);
    }
}