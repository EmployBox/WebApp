package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.ChatBinder;
import isel.ps.employbox.model.binder.MessageBinder;
import isel.ps.employbox.model.input.InChat;
import isel.ps.employbox.model.input.InMessage;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutMessage;
import isel.ps.employbox.services.ChatService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

@RestController
@RequestMapping("/accounts/{id}/chats")
public class ChatController {
    private final ChatBinder chatBinder;
    private final ChatService chatService;
    private final MessageBinder messageBinder;

    public ChatController(ChatBinder chatBinder, ChatService chatService, MessageBinder messageBinder) {
        this.chatBinder = chatBinder;
        this.chatService = chatService;
        this.messageBinder = messageBinder;
    }


    @GetMapping("/{cid}/messages")
    public HalCollection getChatsMessages (@PathVariable long id, @PathVariable long cid, Authentication authentication) {
        return messageBinder.bindOutput(
                chatService.getAccountChatsMessages(id, cid, authentication.getName()),
                this.getClass(),
                id,
                cid
        );
    }

    @PostMapping
    public void createChat(@PathVariable long id, @RequestBody InChat inChat, Authentication authentication) {
        if(id != inChat.getAccountIdFirst()) throw new BadRequestException(badRequest_IdsMismatch);
        chatService.createNewChat(
                id,
                chatBinder.bindInput(inChat),
                authentication.getName()
        );
    }

    @PostMapping("/{cid}/messages")
    public void createMessage(@PathVariable long id, @PathVariable long cid,  @RequestBody InMessage msg, Authentication authentication) {
        if(cid != msg.getChatId())
        chatService.createNewChatMessage(id, cid, messageBinder.bindInput(msg), authentication.getName());
    }

    @GetMapping("/{cid}/messages/{mid}")
    public OutMessage getChatMessage(@PathVariable long cid, @PathVariable long mid, Authentication authentication) {
        return messageBinder.bindOutput( chatService.getAccountChatsMessage(cid, mid, authentication.getName()));
    }
}
