package model;

public class Follow extends DomainObject<String> {

    private final long accountIdFrom;
    private final long accountIdDest;

    private Follow(long accountIdFrom, long accountIdDest) {
        super(String.format("%d %d", accountIdFrom, accountIdDest), 0);
        this.accountIdFrom = accountIdFrom;
        this.accountIdDest = accountIdDest;
    }

    public static Follow create(long accountIdFrom, long accountIdDest){
        Follow follow = new Follow(accountIdFrom, accountIdDest);
        follow.markNew();
        return follow;
    }

    public static Follow load(long accountIdFrom, long accountIdDest){
        Follow follow = new Follow(accountIdFrom, accountIdDest);
        follow.markClean();
        return follow;
    }

    public long getAccountIdFrom() {
        return accountIdFrom;
    }

    public long getAccountIdDest() {
        return accountIdDest;
    }
}
