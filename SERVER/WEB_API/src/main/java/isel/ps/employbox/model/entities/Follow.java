package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.EmbeddedId;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.utils.EmbeddedIdClass;

public class Follow implements DomainObject<Follow.FollowKey> {

    @EmbeddedId
    private final FollowKey followKey;
    @Version
    private final long version;

    public Follow(){
        followKey = new FollowKey();
        version = 0;
    }

    public Follow(long accountIdFollower, long accountIdFollowed, long version) {
        this.version = version;
        followKey = new FollowKey(accountIdFollowed, accountIdFollower);
    }

    public Follow(long accountIdFollower, long accountIdFollowed) {
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
        return followKey.getAccountIdFollower();
    }

    public long getAccountIdFollowed() {
        return followKey.getAccountIdFollowed();
    }

    public static class FollowKey extends EmbeddedIdClass {
        private final long accountIdFollower;
        private final long accountIdFollowed;

        public FollowKey(){
            super();
            accountIdFollowed = 0;
            accountIdFollower = 0;
        }

        public FollowKey(long accountIdFollowed, long accountIdFollower) {
            super(accountIdFollowed, accountIdFollower);
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
