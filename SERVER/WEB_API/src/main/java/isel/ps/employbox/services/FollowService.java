package isel.ps.employbox.services;

import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Follow;
import org.github.isel.rapper.DataRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class FollowService {
    private final DataRepository<Follow, Follow.FollowKey> followsRepo;
    private final UserService accountService;

    public FollowService(DataRepository<Follow, Follow.FollowKey> followeRepo, UserService userService) {
        this.followsRepo = followeRepo;
        this.accountService = userService;
    }

    public Stream<Account> getAccountFollowers(long followedAccountId) {
       return StreamSupport.stream( followsRepo.findAll().join().spliterator(), false)
               .filter(curr-> curr.getAccountIdFollowed() == followedAccountId)
               .map( curr-> accountService.getUser(curr.getAccountIdFollower()));
    }

    public Stream<Account> getAccountFollowing(long followerAccountId, String email) {
        return StreamSupport.stream( followsRepo.findAll().join().spliterator(), false)
                .filter(curr-> curr.getAccountIdFollower() == followerAccountId)
                .map( curr-> accountService.getUser(curr.getAccountIdFollowed(), email));
    }

    public void setFollower(long accountToBeFollowedId, long accountToFollowId, String name){
        followsRepo.create( new Follow(accountToBeFollowedId, accountToFollowId));
    }

    public void deleteFollower(long id, long fid, String name) {
        followsRepo.deleteById( new Follow.FollowKey(id,fid) );
    }
}
