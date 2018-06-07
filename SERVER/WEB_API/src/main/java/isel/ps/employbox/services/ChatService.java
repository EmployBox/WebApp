package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.Transaction;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.entities.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    //todo endpoint
    public CompletableFuture<CollectionPage<Chat>> getAccountChats(long accountId, int pageSize, int page) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];
        return accountService.getAccount(accountId)
                .thenCompose(__ -> new Transaction(Connection.TRANSACTION_SERIALIZABLE)
                        .andDo(() -> chatRepo.findAll(page, pageSize)
                                .thenCompose(listRes -> {
                                    list[0] = listRes;
                                    return chatRepo.getNumberOfEntries();
                                })
                                .thenAccept(collectionSize ->
                                        ret[0] = new CollectionPage(
                                                collectionSize,
                                                pageSize,
                                                page,
                                                list[0])
                                )
                        ).commit())
                .thenApply( __ -> ret[0]);
    }

    public CompletableFuture<CollectionPage<Message>> getAccountChatsMessages(long accountId, String email, int pageSize, int page) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];
        return accountService.getAccount(accountId, email)
                .thenCompose(__ -> new Transaction(Connection.TRANSACTION_SERIALIZABLE)
                        .andDo(() -> msgRepo.findWhere(page, pageSize, new Pair<>("accountId", accountId))
                                .thenCompose(listRes -> {
                                    list[0] = listRes;
                                    return msgRepo.getNumberOfEntries( new Pair<>("accountId", accountId));
                                })
                                .thenAccept(numberOfEntries ->
                                        ret[0] = new CollectionPage(
                                                numberOfEntries,
                                                pageSize,
                                                page,
                                                list[0])
                                ))
                        .commit()
                ).thenApply(__ -> ret[0]);
    }


    public CompletableFuture<Message> getAccountChatsMessage(long cid, long mid, String email) {
        return msgRepo.findById(mid)
                .thenApply(omsg -> omsg.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND_MESSAGE)))
                .thenCompose(msg -> {
                    if (msg.getChatId() != cid)
                        throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
                    return accountService.getAccount(msg.getAccountId(), email)//throws exceptions
                            .thenApply(account -> msg);
                });
    }

    public CompletableFuture<Message> createNewChatMessage(long accountId, long chatId, Message msg, String email) {
        return accountService.getAccount(accountId, email)
                .thenCompose(account -> getChat(chatId))
                .thenCompose(chat -> {
                    if (chat.getAccountIdFirst() != accountId)
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_MESSAGE);
                    return msgRepo.create(msg);
                })
                .thenApply(res -> msg);
    }

    private CompletableFuture<Chat> getChat(long chatId){
        return chatRepo.findById(chatId)
                .thenApply(ochat -> ochat.orElseThrow( () -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND_CHAT)));
    }

    public Mono<Chat> createNewChat(long accountIdFrom, Chat inChat, String username) {
        return Mono.fromFuture(
                CompletableFuture.allOf(
                        accountService.getAccount(accountIdFrom, username),
                        accountService.getAccount(inChat.getAccountIdSecond())
                )
                        .thenCompose(aVoid -> chatRepo.create(inChat))
                        .thenApply(res -> inChat)
        );
    }
}
