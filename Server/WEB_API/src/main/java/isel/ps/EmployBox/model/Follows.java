package isel.ps.EmployBox.model;

public class Follows extends DomainObject<String> {

    private final long accountIdFrom;
    private final long accountIdDest;

    public Follows(long accountIdFrom, long accountIdDest) {
        super(String.format("%d %d", accountIdFrom, accountIdDest), 0);
        this.accountIdFrom = accountIdFrom;
        this.accountIdDest = accountIdDest;
    }

    public static Follows create(long accountIdFrom, long accountIdDest){
        Follows follows = new Follows(accountIdFrom, accountIdDest);
        follows.markNew();
        return follows;
    }

    public static Follows load(long accountIdFrom, long accountIdDest){
        Follows follows = new Follows(accountIdFrom, accountIdDest);
        follows.markClean();
        return follows;
    }

    public long getAccountIdFrom() {
        return accountIdFrom;
    }

    public long getAccountIdDest() {
        return accountIdDest;
    }
}
