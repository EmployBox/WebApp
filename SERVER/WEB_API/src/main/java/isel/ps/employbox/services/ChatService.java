package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.entities.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;
import static isel.ps.employbox.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class ChatService {
    private final AccountService accountService;

    public ChatService( AccountService accountService) {
        this.accountService = accountService;
    }

    //todo endpoint
    public CompletableFuture<CollectionPage<Chat>> getAccountChats(long accountId, int page, int pageSize) {

        return accountService.getAccount(accountId)
                .thenCompose(__ -> ServiceUtils.getCollectionPageFuture( Chat.class, page, pageSize, new Pair<>("accountId", accountId)));
    }

    public CompletableFuture<CollectionPage<Message>> getAccountChatsMessages(long accountId, String email, int page, int pageSize) {
        return accountService.getAccount(accountId, email)
                .thenCompose(__ -> ServiceUtils.getCollectionPageFuture( Message.class, page, pageSize, new Pair<>("accountId", accountId)));
    }


    public CompletableFuture<Message> getAccountChatsMessage(long cid, long mid, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Message, Long> msgMapper = getMapper(Message.class, unitOfWork);
        CompletableFuture<Message> future = msgMapper.findById(mid)
                .thenApply(omsg -> omsg.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND_MESSAGE)))
                .thenCompose(msg -> {
                    if (msg.getChatId() != cid)
                        throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
                    return accountService.getAccount(msg.getAccountId(), email)//throws exceptions
                            .thenApply(account -> msg);
                }).thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res));
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<Message> createNewChatMessage(long accountId, long chatId, Message msg, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Message, Long> msgMapper = getMapper(Message.class, unitOfWork);
        CompletableFuture<Message> future = accountService.getAccount(accountId, email)
                .thenCompose(account -> getChat(chatId))
                .thenCompose(chat -> {
                    if (chat.getAccountIdFirst() != accountId)
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_MESSAGE);
                    return msgMapper.create( msg);
                }).thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> msg));
        return handleExceptions(future, unitOfWork);
    }

    private CompletableFuture<Chat> getChat(long chatId){
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Chat, Long> chatMapper = getMapper(Chat.class, unitOfWork);
        CompletableFuture<Chat> future = chatMapper.findById(chatId)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(ochat -> ochat.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND_CHAT)));
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Chat> createNewChat(long accountIdFrom, Chat inChat, String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Chat, Long> chatMapper = getMapper(Chat.class, unitOfWork);
        CompletableFuture<Chat> future = CompletableFuture.allOf(
                accountService.getAccount(accountIdFrom, username),
                accountService.getAccount(inChat.getAccountIdSecond())
        )
                .thenCompose(aVoid -> chatMapper.create(inChat))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> inChat));
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
