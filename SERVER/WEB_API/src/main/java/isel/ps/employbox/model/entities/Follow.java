package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;

public class Follow implements DomainObject<Follow.FollowKey>{
    private final FollowKey followKey;
    private final long accountIdFollower;
    private final long accountIdFollowed;
    private final long version;

    public Follow(){
        followKey = null;
        accountIdFollower = 0;
        accountIdFollowed = 0;
        version = 0;
    }

    public Follow(long accountIdFollower, long accountIdFollowed, long version) {
        this.accountIdFollower = accountIdFollower;
        this.accountIdFollowed = accountIdFollowed;
        this.version = version;
        followKey = new FollowKey(accountIdFollowed, accountIdFollower);
    }

    public Follow(long accountIdFollower, long accountIdFollowed) {
        this.accountIdFollower = accountIdFollower;
        this.accountIdFollowed = accountIdFollowed;
        this.version = -1;
        followKey = new FollowKey(accountIdFollowed, accountIdFollower);
    }

    @Override
    public FollowKey getIdentityKey() {
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

    public static class FollowKey {
        private long accountIdFollower;
        private long accountIdFollowed;

        public FollowKey(){}

        public FollowKey(long accountIdFollowed, long accountIdFollower) {
            this.accountIdFollower = accountIdFollowed;
            this.accountIdFollowed = accountIdFollower;
        }

        public long getAccountIdFollower() {
            return accountIdFollower;
        }

        public long getAccountIdFollowed() {
            return accountIdFollowed;
        }
    }
}
