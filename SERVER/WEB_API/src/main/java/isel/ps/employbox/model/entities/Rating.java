package isel.ps.employbox.model.entities;

public class Rating {
    private final long accountIdFrom;
    private final long accountIdTo;
    private final long moderatorId; // can be null
    private final double ratingValue;
    private final boolean approved;
    private final long version;

    public Rating(long accountIdFrom,long accountIdTo, long moderatorId, double ratingValue, boolean approved, long version) {
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.moderatorId = moderatorId;
        this.ratingValue = ratingValue;
        this.approved = approved;
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    public long getModeratorId() {
        return moderatorId;
    }

    public double getRatingValue() {
        return ratingValue;
    }

    public long getAccountIdTo() {
        return accountIdTo;
    }

    public long getAccountIdFrom() {
        return accountIdFrom;
    }

    public boolean isApproved() {
        return approved;
    }
}
