package isel.ps.employbox.model.entities;

public class Follows {

    private final long accountIdFrom;
    private final long accountIdDest;
    private final long version;

    public Follows(long accountIdFrom, long accountIdDest, long version) {
        this.accountIdFrom = accountIdFrom;
        this.accountIdDest = accountIdDest;
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    public long getAccountIdFrom() {
        return accountIdFrom;
    }

    public long getAccountIdDest() {
        return accountIdDest;
    }
}
