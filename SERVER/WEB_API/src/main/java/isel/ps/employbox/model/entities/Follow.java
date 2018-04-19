package isel.ps.employbox.model.entities;

import isel.ps.employbox.model.entities.composedKeys.UserToUserKey;
import org.github.isel.rapper.DomainObject;

public class Follow implements DomainObject<UserToUserKey>{
    private final UserToUserKey followKey;
    private final long accountIdFollower;
    private final long accountIdFollowed;
    private final long version;

    public Follow(long accountIdFollower, long accountIdFollowed, long version) {
        this.accountIdFollower = accountIdFollower;
        this.accountIdFollowed = accountIdFollowed;
        this.version = version;
        followKey = new UserToUserKey(accountIdFollowed, accountIdFollower);
    }

    public Follow(long accountIdFollower, long accountIdFollowed) {
        this.accountIdFollower = accountIdFollower;
        this.accountIdFollowed = accountIdFollowed;
        this.version = -1;
        followKey = new UserToUserKey(accountIdFollowed, accountIdFollower);
    }

    @Override
    public UserToUserKey getIdentityKey() {
        return followKey;
    }

    public long getVersion() {
        return version;
    }

    public long getAccountIdFollower() {
        return accountIdFollower;
    }

    public long getAccountIdFollowed() {
        return accountIdFollowed;
    }
}
