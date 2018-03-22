package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.input.InChat;
import isel.ps.employbox.api.model.input.InMessage;
import isel.ps.employbox.api.model.output.OutAccount;
import isel.ps.employbox.api.model.output.OutChat;
import isel.ps.employbox.api.model.output.OutJob;
import isel.ps.employbox.api.model.output.OutMessage;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final APIService apiService;

    private AccountController(AccountService accountService, APIService apiService){
        this.accountService = accountService;
        this.apiService = apiService;
    }

    @GetMapping("/account")
    public List<OutAccount> getAccountsPage (@RequestParam Map<String,String> queryString){
        return accountService.getAccountsPage (queryString);
    }

    @GetMapping("/account/{id}")
    public OutAccount getAccount(@PathVariable long id){ return accountService.getAccount(id); }

    @GetMapping("/account/{id}/job")
    public List<OutJob> getAccountOffers(@PathVariable long id){
        return null;//accountService.getAccountOffers(id);
    }

    @GetMapping("/account/{id}/followers")
    public List<OutAccount> getFollowers(@PathVariable long id){ return accountService.getAccountFollowers(id); }

    @PostMapping("/account/{id}/followers/{fid}")
    public void setFollower(
            @PathVariable long id,
            @PathVariable long fid,
            @RequestHeader("apiKey") String apiKey
    ){
        accountService.setFollower(id,fid);
    }

    @DeleteMapping("/account/{id}/followers/{fid}")
    public void deleteFollower(@PathVariable long id,
                               @PathVariable long fid,
                               @RequestHeader("apiKey") String apiKey
    ){
        apiService.validateAPIKey(apiKey);
        accountService.deleteFollower(id,fid);
    }

    @GetMapping("/account/{id}/following")
    public List<OutAccount> getFollowing(@PathVariable long id){ return accountService.getAccountFollowing(id); }

    @GetMapping("/account/{id}/chat")
    public List<OutChat> getChats (@PathVariable long id){ return accountService.getAccountChats(id); }

    @GetMapping("/account/{id}/chat/{cid}/messages")
    public List<OutMessage> getChatsMessages (@PathVariable long id, @RequestParam Map<String,String> queryString) {
        return accountService.getAccountChatsMessages(id, queryString);
    }

    @PostMapping("/account/{id}/chat")
    public void setChat (
            @PathVariable long id,
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InChat inChat
    ) {
        apiService.validateAPIKey(apiKey);
        accountService.createNewChat(inChat);
    }

    @PostMapping("/account/{id}/chats/{cid}/message")
    public void setChatMessage (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InMessage msg
    ) {
        apiService.validateAPIKey(apiKey);
        accountService.createNewChatMessage(id,cid,msg);
    }
}
