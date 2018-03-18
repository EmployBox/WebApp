package isel.ps.EmployBox.api.services;

import isel.ps.EmployBox.api.model.input.InChat;
import isel.ps.EmployBox.api.model.input.InMessage;
import isel.ps.EmployBox.api.model.output.Account;
import isel.ps.EmployBox.api.model.output.OutChat;
import isel.ps.EmployBox.api.model.output.Job;
import isel.ps.EmployBox.api.model.output.OutMessage;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

@Service
public final class AccountService {

    public List<Account> getAccountsPage(Map<String,String> queryString) {
        String page = queryString.get("page");
        if (page == null)
            page = "0";


        return null;
    }

    public final Account getAccount(long id) {
        throw new NotImplementedException();
    }

    public final List<Job> getAccountOffers(long id) {
        throw new NotImplementedException();
    }

    public List<Account> getAccountFollowers(long id) {
        throw new NotImplementedException();
    }

    public List<Account> getAccountFollowing(long id) {
        throw new NotImplementedException();
    }

    public List<OutChat> getAccountChats(long id) {
        throw new NotImplementedException();
    }

    public List<OutMessage> getAccountChatsMessages(long id, Map<String,String> queryString) {
        throw new NotImplementedException();
    }

    public String createNewChat(InChat inChat) {
        throw new NotImplementedException();
    }

    public String createNewChatMessage(InMessage msg) {
        throw new NotImplementedException();
    }
}
