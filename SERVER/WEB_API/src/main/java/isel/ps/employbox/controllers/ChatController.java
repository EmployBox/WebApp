package isel.ps.employbox.controllers;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.ChatBinder;
import isel.ps.employbox.model.binder.MessageBinder;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.input.InChat;
import isel.ps.employbox.model.input.InMessage;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutMessage;
import isel.ps.employbox.services.ChatService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

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
    public Mono<HalCollectionPage> getChatsMessages (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        return messageBinder.bindOutput(
                chatService.getAccountChatsMessages(id, cid, authentication.getName(), page),
                this.getClass(),
                id,
                cid
        );
    }

    @PostMapping
    public Mono<Chat> createChat(@PathVariable long id, @RequestBody InChat inChat, Authentication authentication) {
        if(id != inChat.getAccountIdFirst()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return chatService.createNewChat(
                id,
                chatBinder.bindInput(inChat),
                authentication.getName()
        );
    }

    @PostMapping("/{cid}/messages")
    public Mono<OutMessage> createMessage(@PathVariable long id, @PathVariable long cid, @RequestBody InMessage msg, Authentication authentication) {
        if (cid != msg.getChatId())
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        return messageBinder.bindOutput(chatService.createNewChatMessage(id, cid, messageBinder.bindInput(msg), authentication.getName()));
    }

    @GetMapping("/{cid}/messages/{mid}")
    public Mono<OutMessage> getChatMessage(@PathVariable long cid, @PathVariable long mid, Authentication authentication) {
        return messageBinder.bindOutput( chatService.getAccountChatsMessage(cid, mid, authentication.getName()));
    }
}
