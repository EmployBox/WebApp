package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.binder.AccountBinder;
import isel.ps.employbox.api.model.binder.ChatBinder;
import isel.ps.employbox.api.model.binder.JobBinder;
import isel.ps.employbox.api.model.binder.MessageBinder;
import isel.ps.employbox.api.model.input.InChat;
import isel.ps.employbox.api.model.input.InMessage;
import isel.ps.employbox.api.model.output.*;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.AccountService;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountBinder accountBinder;
    private final ChatBinder chatBinder;
    private final MessageBinder messageBinder;
    private final JobBinder jobBinder;
    private final APIService apiService;

    private AccountController(AccountService accountService, AccountBinder accountBinder, ChatBinder chatBinder, MessageBinder messageBinder, JobBinder jobBinder, APIService apiService){
        this.accountService = accountService;
        this.accountBinder = accountBinder;
        this.chatBinder = chatBinder;
        this.messageBinder = messageBinder;
        this.jobBinder = jobBinder;
        this.apiService = apiService;
    }


    @GetMapping("/account")
    public List<OutAccount> getAccounts(){
        return accountBinder.bindOutput(accountService.getAccountsPage());
    }

    @GetMapping("/account/{id}")
    public OutAccount getAccount(@PathVariable long id){
        return accountBinder.bindOutput(accountService.getAccount(id).get()); }

    @GetMapping("/account/{id}/job")
    public List<OutJob> getAccountOffers(@PathVariable long id){
        return jobBinder.bindOutput(
                accountService.getAccountOffers(id)
        );
    }

    @GetMapping("/account/{id}/followers")
    public List<OutAccount> getFollowers(@PathVariable long id){
        return accountBinder.bindOutput(accountService.getAccountFollowers(id));
    }

    @GetMapping("/account/{id}/following")
    public List<OutAccount> getFollowing(@PathVariable long id){
        return accountBinder.bindOutput(accountService.getAccountFollowing(id));
    }

    @GetMapping("/account/{id}/chat")
    public List<OutChat> getChats (@PathVariable long id){
                return chatBinder.bindOutput(accountService.getAccountChats(id));
    }

    @GetMapping("/account/{id}/chat/{cid}/messages")
    public List<OutMessage> getChatsMessages (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam Map<String,String> queryString) {
        return messageBinder.bindOutput(accountService.getAccountChatsMessages(id,cid, queryString));
    }

    @GetMapping("/account/{id}/ratings")
    public List<OutRating> getRatings (
            HttpServletResponse res,
            @PathVariable long id,
            @RequestParam Map<String,String> queryString
    ){
        String type = queryString.get("type");
        if(type == null || !( type.equals("done") || type.equals("received") ) ) {
            try {
                res.sendError(BAD_REQUEST.value(),"supplied wrong rating type");
            } catch (IOException e) {  e.printStackTrace(); }
        }
        throw new NotImplementedException();
    }


    @PostMapping("/account/{id}/chat")
    public void setChat (
            @PathVariable long id,
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InChat inChat
    ) {
        apiService.validateAPIKey(apiKey);
        accountService.createNewChat(
                chatBinder.bindInput(inChat)
        );
    }

    @PostMapping("/account/{id}/chats/{cid}/message")
    public void setChatMessage (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InMessage msg
    ) {
        apiService.validateAPIKey(apiKey);
        accountService.createNewChatMessage(
                id,
                cid,
                messageBinder.bindInput(msg)
        );
    }

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
}
