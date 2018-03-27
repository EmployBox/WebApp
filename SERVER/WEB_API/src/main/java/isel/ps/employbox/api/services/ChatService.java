package isel.ps.employbox.api.services;

import isel.ps.employbox.dal.model.Chat;
import isel.ps.employbox.dal.model.Message;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    public List<Chat> getAccountChats(long id) {
        throw new NotImplementedException();
    }

    public List<Message> getAccountChatsMessages(long id, long cid, Map<String,String> queryString) {
        throw new NotImplementedException();
    }

    public void createNewChat(Chat inChat) {
        throw new NotImplementedException();
    }

    public void createNewChatMessage(long accountId, long chatId, Message msg) { throw new NotImplementedException();  }
}
