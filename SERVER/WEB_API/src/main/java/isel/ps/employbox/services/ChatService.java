package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.entities.Message;
import org.github.isel.rapper.DataRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;
import static isel.ps.employbox.ErrorMessages.resourceNotFound_message;

@Service
public class ChatService {
    private final DataRepository<Chat, Long> chatRepo;
    private final DataRepository<Message, Long> msgRepo;
    private final AccountService accountService;

    public ChatService(DataRepository<Chat, Long> chatRepo, DataRepository<Message, Long> msgRepo, AccountService accountService) {
        this.chatRepo = chatRepo;
        this.msgRepo = msgRepo;
        this.accountService = accountService;
    }


    public CompletableFuture<Stream<Chat>> getAccountChats(long accountId) {
        return chatRepo.findAll()
                .thenApply(list -> list
                        .stream()
                        .filter(curr -> curr.getAccountIdFirst() == accountId));
    }

    public CompletableFuture<Stream<Message>> getAccountChatsMessages(long accountId, long cid, String email) {
        return accountService.getAccount(accountId, email).thenCompose(__ ->
                msgRepo.findAll()
                        .thenApply(list -> list
                                .stream()
                                .filter(curr -> curr.getAccountId() == accountId && curr.getChadId() == cid))
        );
    }


    public CompletableFuture<Message> getAccountChatsMessage(long cid, long mid, String email) {
        return msgRepo.findById(mid)
                .thenApply(omsg -> omsg.orElseThrow(() -> new ResourceNotFoundException(resourceNotFound_message)))
                .thenCompose(msg -> {
                    if (msg.getChadId() != cid)
                        throw new BadRequestException(badRequest_IdsMismatch);
                    return accountService.getAccount(msg.getAccountId(), email)//throws exceptions
                            .thenApply(__ -> msg);
                });
    }

    public CompletableFuture<Message> createNewChatMessage(long accountId, long chatId, Message msg, String email) {
        return accountService.getAccount(accountId, email)
                .thenCompose(__ -> getChat(chatId))
                .thenCompose(chat -> {
                    if (chat.getAccountIdFirst() != accountId)
                        throw new UnauthorizedException(ErrorMessages.unAuthorized_message);
                    return msgRepo.create(msg);
                })
                .thenApply(res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                    return msg;
                });
    }

    public CompletableFuture<Chat> getChat(long chatId){
        return chatRepo.findById(chatId)
                .thenApply(ochat -> ochat.orElseThrow( () -> new ResourceNotFoundException(ErrorMessages.resourceNotFound_chat)));
    }

    public Mono<Chat> createNewChat(long accountIdFrom, Chat inChat, String username) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        accountService.getAccount(accountIdFrom, username),
                        accountService.getAccount(inChat.getAccountIdSecond())
                ).thenCompose(__ -> chatRepo.create(inChat)
                ).thenApply(res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                    return inChat;
                })
        );
    }
}
