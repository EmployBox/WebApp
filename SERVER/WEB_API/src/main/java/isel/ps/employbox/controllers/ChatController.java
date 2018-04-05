package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.ChatBinder;
import isel.ps.employbox.model.binder.MessageBinder;
import isel.ps.employbox.model.input.InChat;
import isel.ps.employbox.model.input.InMessage;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutMessage;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.ChatService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/{id}/chats")
public class ChatController {
    private final ChatBinder chatBinder;
    private final ChatService chatService;
    private final MessageBinder messageBinder;
    private final APIService apiService;

    public ChatController(ChatBinder chatBinder, ChatService chatService, MessageBinder messageBinder, APIService apiService) {
        this.chatBinder = chatBinder;
        this.chatService = chatService;
        this.messageBinder = messageBinder;
        this.apiService = apiService;
    }


    @GetMapping("/{cid}/messages")
    public Resource<HalCollection> getChatsMessages (@PathVariable long id, @PathVariable long cid, @RequestParam String page) {
        return messageBinder.bindOutput(
                chatService.getAccountChatsMessages(id,cid, page),
                this.getClass(),
                id,
                cid
        );
    }

    @PostMapping
    public void createChat(@PathVariable long id, @RequestHeader("apiKey") String apiKey, @RequestBody InChat inChat) {
        if(id != inChat.getAccountIdFirst()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        apiService.validateAPIKey(apiKey);
        chatService.createNewChat(
                chatBinder.bindInput(inChat)
        );
    }

    @PostMapping("/{cid}/messages")
    public void createMessage(@PathVariable long id, @PathVariable long cid, @RequestHeader("apiKey") String apiKey, @RequestBody InMessage msg) {
        apiService.validateAPIKey(apiKey);
        chatService.createNewChatMessage(id, cid, messageBinder.bindInput(msg));
    }

    @GetMapping("/{cid}/messages/{mid}")
    public Resource<OutMessage> getChatMessage(@PathVariable long cid, @PathVariable long mid) {
        return messageBinder.bindOutput( chatService.getAccountChatsMessage(cid, mid) );
    }
}
