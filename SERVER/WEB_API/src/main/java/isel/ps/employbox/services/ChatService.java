package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
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

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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

    //todo
    public CompletableFuture<Stream<Chat>> getAccountChats(long accountId) {
        return chatRepo.findAll()
                .thenApply(list -> list
                        .stream()
                        .filter(curr -> curr.getAccountIdFirst() == accountId));
    }

    public CompletableFuture<CollectionPage<Message>> getAccountChatsMessages(long accountId, long cid, String email, int page) {
        return accountService.getAccount(accountId, email).thenCompose(account ->
                msgRepo.findWhere(page, CollectionPage.PAGE_SIZE, new Pair<>("accountId",accountId))
                        .thenApply(list -> new CollectionPage(
                                list.size(),
                                page,
                                list.stream()
                                        .skip(CollectionPage.PAGE_SIZE * page)
                                        .limit(CollectionPage.PAGE_SIZE)
                                        .collect(Collectors.toList())
                        ))
        );
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
