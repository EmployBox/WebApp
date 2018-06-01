package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.entities.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;
import static isel.ps.employbox.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE;

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
                                .filter(curr -> curr.getAccountId() == accountId && curr.getChatId() == cid))
        );
    }


    public CompletableFuture<Message> getAccountChatsMessage(long cid, long mid, String email) {
        return msgRepo.findById(mid)
                .thenApply(omsg -> omsg.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND_MESSAGE)))
                .thenCompose(msg -> {
                    if (msg.getChatId() != cid)
                        throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
                    return accountService.getAccount(msg.getAccountId(), email)//throws exceptions
                            .thenApply(__ -> msg);
                });
    }

    public CompletableFuture<Message> createNewChatMessage(long accountId, long chatId, Message msg, String email) {
        return accountService.getAccount(accountId, email)
                .thenCompose(__ -> getChat(chatId))
                .thenCompose(chat -> {
                    if (chat.getAccountIdFirst() != accountId)
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_MESSAGE);
                    return msgRepo.create(msg);
                })
                .thenApply(res -> msg);
    }

    public CompletableFuture<Chat> getChat(long chatId){
        return chatRepo.findById(chatId)
                .thenApply(ochat -> ochat.orElseThrow( () -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND_CHAT)));
    }

    public Mono<Chat> createNewChat(long accountIdFrom, Chat inChat, String username) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        accountService.getAccount(accountIdFrom, username),
                        accountService.getAccount(inChat.getAccountIdSecond())
                )
                        .thenCompose(__ -> chatRepo.create(inChat))
                        .thenApply(res -> inChat)
        );
    }
}
