package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.exceptions.BadRequestException;
import isel.ps.employbox.api.model.ModelBinder;
import isel.ps.employbox.api.model.binder.MessageBinder;
import isel.ps.employbox.api.model.input.InChat;
import isel.ps.employbox.api.model.input.InMessage;
import isel.ps.employbox.api.model.output.OutChat;
import isel.ps.employbox.api.model.output.OutMessage;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.ChatService;
import isel.ps.employbox.dal.model.Chat;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static isel.ps.employbox.api.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/{id}/chats")
public class ChatController {
    private final ModelBinder<Chat, OutChat, InChat, Long> chatBinder;
    private final ChatService chatService;
    private final MessageBinder messageBinder;
    private final APIService apiService;

    public ChatController(ModelBinder<Chat, OutChat, InChat, Long> chatBinder, ChatService chatService, MessageBinder messageBinder, APIService apiService) {
        this.chatBinder = chatBinder;
        this.chatService = chatService;
        this.messageBinder = messageBinder;
        this.apiService = apiService;
    }

    @GetMapping
    public List<OutChat> getChats (@PathVariable long id){
        return chatBinder.bindOutput(chatService.getAccountChats(id));
    }

    @GetMapping("/{cid}/messages")
    public List<OutMessage> getChatsMessages (@PathVariable long id, @PathVariable long cid, @RequestParam String page) {
        return messageBinder.bindOutput(chatService.getAccountChatsMessages(id,cid, page));
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
}
