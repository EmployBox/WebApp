package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.entities.Message;
import org.github.isel.rapper.DataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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


    public Stream<Chat> getAccountChats(long accountId) {
        return StreamSupport.stream(chatRepo.findAll().join().spliterator(), false)
                .filter(curr-> curr.getAccountIdFirst() == accountId);
    }

    public Stream<Message> getAccountChatsMessages(long accountId, long cid, String email) {
        accountService.getAccount(accountId, email);

        return StreamSupport.stream(msgRepo.findAll().join().spliterator(),false)
                .filter(curr-> curr.getAccountId() == accountId && curr.getChadId() == cid);
    }

    public Message getAccountChatsMessage(long cid, long mid, String email) {
        Optional<Message> omsg = msgRepo.findById(mid).join();
        if(!omsg.isPresent())
            throw new ResourceNotFoundException(resourceNotFound_message);

        Message msg = omsg.get();
        accountService.getAccount(msg.getAccountId(), email);

        if(msg.getChadId() != cid )
            throw new BadRequestException( badRequest_IdsMismatch );

        return msg;
    }

    public void createNewChatMessage(long accountId, long chatId, Message msg, String email) {
        accountService.getAccount(accountId, email);

        Chat chat = getChat(chatId);
        if(chat.getAccountIdFirst() != accountId)
            throw new UnauthorizedException( ErrorMessages.unAuthorized_message );
        msgRepo.create(msg);
    }

    public Chat getChat(long chatId){
        Optional<Chat> ochat = chatRepo.findById(chatId).join();
        if(!ochat.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotFound_chat);
        return ochat.get();
    }

    public void createNewChat(long accountIdFrom, Chat inChat, String username) {
        accountService.getAccount(accountIdFrom,username);

        accountService.getAccount(inChat.getAccountIdSecond());
        chatRepo.create(inChat);
    }
}
