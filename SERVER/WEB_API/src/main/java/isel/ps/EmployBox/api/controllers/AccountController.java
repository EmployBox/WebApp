package isel.ps.EmployBox.api.controllers;

import isel.ps.EmployBox.api.model.input.InChat;
import isel.ps.EmployBox.api.model.input.InMessage;
import isel.ps.EmployBox.api.model.output.Account;
import isel.ps.EmployBox.api.model.output.OutChat;
import isel.ps.EmployBox.api.model.output.Job;
import isel.ps.EmployBox.api.model.output.OutMessage;
import isel.ps.EmployBox.api.services.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    private final AccountService accountService;

    private AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/account")
    public List<Account> getAccountsPage (@RequestParam Map<String,String> queryString){
        return accountService.getAccountsPage (queryString);
    }

    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable long id){ return accountService.getAccount(id); }

    @GetMapping("/account/{id}/job")
    public List<Job> getAccountOffers(@PathVariable long id){ return accountService.getAccountOffers(id); }

    @GetMapping("/account/{id}/followers")
    public List<Account> getFollowers(@PathVariable long id){ return accountService.getAccountFollowers(id); }

    @PostMapping("/account/{id}/followers/{fid}")
    public void setFollower(
            @PathVariable long id,
            @PathVariable long fid,
            @RequestHeader("apiKey") String apiKey){
        accountService.setFollower(id,fid);
    }

    @DeleteMapping("/account/{id}/followers/{fid}")
    public void deleteFollower(@PathVariable long id,
                                        @PathVariable long fid,
                                        @RequestHeader("apiKey") String apiKey
    ){  accountService.deleteFollower(id,fid); }

    @GetMapping("/account/{id}/following")
    public List<Account> getFollowing(@PathVariable long id){ return accountService.getAccountFollowing(id); }

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
            @RequestBody InChat inChat)
    { accountService.createNewChat(inChat); }

    @PostMapping("/account/{id}/chats/{cid}/message")
    public void setChatMessage (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InMessage msg)
    {  accountService.createNewChatMessage(msg); }


}
