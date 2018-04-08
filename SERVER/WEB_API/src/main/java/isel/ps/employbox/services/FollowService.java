package isel.ps.employbox.services;

import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Follow;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class FollowService {
    private final RapperRepository<Follow, Pair<Long,Long>> followsRepo;
    private final AccountService accountService;

    public FollowService(RapperRepository<Follow, Pair<Long, Long>> followeRepo, AccountService accountService) {
        this.followsRepo = followeRepo;
        this.accountService = accountService;
    }



    public Stream<Account> getAccountFollowers(long followedAccountId) {
       return StreamSupport.stream( followsRepo.findAll().spliterator(), false)
               .filter(curr-> curr.getAccountIdFollowed() == followedAccountId)
               .map( curr-> accountService.getAccount(curr.getAccountIdFollower()));
    }

    public Stream<Account> getAccountFollowing(long followerAccountId) {
        return StreamSupport.stream( followsRepo.findAll().spliterator(), false)
                .filter(curr-> curr.getAccountIdFollower() == followerAccountId)
                .map( curr-> accountService.getAccount(curr.getAccountIdFollowed()));
    }

    public void setFollower(long accountToBeFollowedId, long accountToFollowId){
        followsRepo.create( new Follow(accountToBeFollowedId, accountToFollowId));
    }

    public void deleteFollower(long id, long fid) {
        followsRepo.deleteById( new Pair(id,fid) );
    }
}
