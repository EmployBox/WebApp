package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.entities.Message;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;
import static isel.ps.employbox.ErrorMessages.resourceNotFound_message;

@Service
public class ChatService {
    private final RapperRepository<Chat, Long> chatRepo;
    private final RapperRepository<Message, Long> msgRepo;
    private final AccountService accountService;

    public ChatService(RapperRepository<Chat, Long> chatRepo, RapperRepository<Message, Long> msgRepo, AccountService accountService) {
        this.chatRepo = chatRepo;
        this.msgRepo = msgRepo;
        this.accountService = accountService;
    }


    public Stream<Chat> getAccountChats(long id) {
        return StreamSupport.stream(chatRepo.findAll().spliterator(), false);
    }

    public Stream<Message> getAccountChatsMessages(long id, long cid) {
        return StreamSupport.stream(msgRepo.findAll().spliterator(),false)
                .filter(curr-> curr.getAccountId() == id && curr.getChadId() == cid);
    }

    public Message getAccountChatsMessage( long cid, long mid) {
        Optional<Message> omsg = msgRepo.findById(mid);
        if(!omsg.isPresent())
            throw new ResourceNotFoundException(resourceNotFound_message);
        Message msg = omsg.get();
        if(msg.getChadId() != cid )
            throw new BadRequestException( badRequest_IdsMismatch );
        return msg;
    }

    public void createNewChatMessage(long accountId, long chatId, Message msg) {
        Chat chat = getChat(chatId);
        if(chat.getAccountIdFirst() != accountId)
            throw new UnauthorizedException( ErrorMessages.unAuthorized_message );
        msgRepo.create(msg);
    }

    public Chat getChat(long chatId){
        Optional<Chat> ochat = chatRepo.findById(chatId);
        if(!ochat.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotFound_chat);
        return ochat.get();
    }

    public void createNewChat(long accountIdFrom, Chat inChat) {
         accountService.getAccount(accountIdFrom);
         accountService.getAccount(inChat.getAccountIdSecond());
         chatRepo.create(inChat);
    }
}
