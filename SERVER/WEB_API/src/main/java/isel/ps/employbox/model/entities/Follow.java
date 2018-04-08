package isel.ps.employbox.model.entities;

public class Follow {

    private final long accountIdFollower;
    private final long accountIdFollowed;
    private final long version;

    public Follow(long accountIdFollower, long accountIdFollowed, long version) {
        this.accountIdFollower = accountIdFollower;
        this.accountIdFollowed = accountIdFollowed;
        this.version = version;
    }

    public Follow(long accountIdFollower, long accountIdFollowed) {
        this.accountIdFollower = accountIdFollower;
        this.accountIdFollowed = accountIdFollowed;
        this.version = -1;
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
