package model;

public class Rating extends DomainObject<String> {
    private final long accountIdFrom;
    private final long accountIdTo;
    private final long moderatorId; // can be null
    private final double ratingValue;

    public Rating(long accountIdFrom,long accountIdTo, long moderatorId, double ratingValue, long version) {
        super(String.format("RatingPK: %d, %d",accountIdFrom,accountIdTo), "", version);
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.moderatorId = moderatorId;
        this.ratingValue = ratingValue;
    }

    public static Rating create(long accountIdFrom,long accountIdTo, long moderatorId, double ratingValue ) {
        Rating rating = new Rating(accountIdFrom,accountIdTo, moderatorId, ratingValue, 0);
        rating.markNew();
        return rating;
    }

    public static Rating load(long accountIdFrom,long accountIdTo, long moderatorId, double ratingValue, long version ) {
        Rating rating = new Rating(accountIdFrom,accountIdTo, moderatorId, ratingValue, version);
        rating.markClean();
        return rating;
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
}
