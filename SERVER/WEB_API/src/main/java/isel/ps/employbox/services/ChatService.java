package isel.ps.employbox.services;

import isel.ps.employbox.model.entities.Chat;
import isel.ps.employbox.model.entities.Message;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class ChatService {

    public List<Chat> getAccountChats(long id) {
        throw new NotImplementedException();
    }

    public List<Message> getAccountChatsMessages(long id, long cid, String queryString) {
        throw new NotImplementedException();
    }

    public Message getAccountChatsMessage( long cid, long mid) {
        throw new NotImplementedException();
    }


    public void createNewChat(Chat inChat) {
        throw new NotImplementedException();
    }

    public void createNewChatMessage(long accountId, long chatId, Message msg) { throw new NotImplementedException();  }
}
