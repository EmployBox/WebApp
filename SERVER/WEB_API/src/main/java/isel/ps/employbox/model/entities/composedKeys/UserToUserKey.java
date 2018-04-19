package isel.ps.employbox.model.entities.composedKeys;

public class UserToUserKey {
    private final long accountIdFollowed;
    private final long accountIdFollower;

    public UserToUserKey(long accountIdFollowed, long accountIdFollower) {

        this.accountIdFollowed = accountIdFollowed;
        this.accountIdFollower = accountIdFollower;
    }

    public long getAccountIdFollowed() {
        return accountIdFollowed;
    }

    public long getAccountIdFollower() {
        return accountIdFollower;
    }
}
