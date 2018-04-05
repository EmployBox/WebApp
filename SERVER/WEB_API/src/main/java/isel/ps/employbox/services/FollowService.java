package isel.ps.employbox.services;

import isel.ps.employbox.model.entities.Account;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.stream.Stream;

@Service
public class FollowService {

    public Stream<Account> getAccountFollowers(long id) {
        throw new NotImplementedException();
    }

    public Stream<Account> getAccountFollowing(long id) {
        throw new NotImplementedException();
    }

    public void setFollower(long id, long fid) {
        throw new NotImplementedException();
    }

    public void deleteFollower(long id, long fid) {
        throw new NotImplementedException();
    }
}
