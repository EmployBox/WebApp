package isel.ps.employbox.api.services;

import isel.ps.employbox.dal.RapperRepository;
import isel.ps.employbox.dal.model.*;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public final class AccountService {
    private RapperRepository <Account, Long> accRepo;
    private RapperRepository <Application, Pair<Long,Long> > applyRepo;
    private RapperRepository <Job,Long> jobRepo;
    private RapperRepository <Chat,Long> chatRepo;
    private RapperRepository <Message, Pair<Long,Long>> msgRepo;
    private RapperRepository <Rating, Pair<Long,Long>> rtngRepo;
    private RapperRepository <Follows,Pair<Long,Long>> fllwRepo;

    public AccountService(RapperRepository<Account, Long> accRepo,
                          RapperRepository<Application, Pair<Long, Long>> applyRepo,
                          RapperRepository<Job, Long> jobRepo,
                          RapperRepository<Chat, Long> chatRepo,
                          RapperRepository<Message, Pair<Long, Long>> msgRepo) {
        this.accRepo = accRepo;
        this.applyRepo = applyRepo;
        this.jobRepo = jobRepo;
        this.chatRepo = chatRepo;
        this.msgRepo = msgRepo;
    }


    public List<Account> getAccountsPage() {

        return null;
    }

    public Optional<Account> getAccount(long id) {
        return accRepo.findById(id);
    }


    public List<Job> getAccountOffers(long id) {
        return jobRepo.findByFK(Account.class, id);
    }

    public List<Account> getAccountFollowers(long id) {
        throw new NotImplementedException();
    }

    public List<Account> getAccountFollowing(long id) {
        throw new NotImplementedException();
    }

    public List<Chat> getAccountChats(long id) {
        throw new NotImplementedException();
    }

    public List<Message> getAccountChatsMessages(long id,long cid, Map<String,String> queryString) {
        throw new NotImplementedException();
    }

    public void createNewChat(Chat inChat) {
        throw new NotImplementedException();
    }

    public void createNewChatMessage(long accountId, long chatId, Message msg) { throw new NotImplementedException();  }

    public void setFollower(long id, long fid) {
        throw new NotImplementedException();
    }

    public void deleteFollower(long id, long fid) {
        throw new NotImplementedException();
    }

    public List<Rating> getRatings(long id) {
        {
            throw new NotImplementedException();
        }
    }
}
