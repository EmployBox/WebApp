package isel.ps.employbox.api.model.output;

public class OutChat {
    private final long accountIdFirst;
    private final long accountIdSecond;

    public OutChat(long accountIdFirst, long accountIdSecond) {
        this.accountIdFirst = accountIdFirst;
        this.accountIdSecond = accountIdSecond;
    }

    public long getAccountIdFirst() {
        return accountIdFirst;
    }

    public long getAccountIdSecond() {
        return accountIdSecond;
    }
}
