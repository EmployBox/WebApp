package isel.ps.employbox.api.services;

import isel.ps.employbox.dal.model.Account;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class FollowService {

    public List<Account> getAccountFollowers(long id) {
        throw new NotImplementedException();
    }

    public List<Account> getAccountFollowing(long id) {
        throw new NotImplementedException();
    }

    public void setFollower(long id, long fid) {
        throw new NotImplementedException();
    }

    public void deleteFollower(long id, long fid) {
        throw new NotImplementedException();
    }
}
