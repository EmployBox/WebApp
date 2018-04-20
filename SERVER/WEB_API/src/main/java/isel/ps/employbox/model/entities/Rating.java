package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;

public class Rating implements DomainObject<Rating.RatingKey>{
    private final RatingKey ratingKey;
    private final long accountIdFrom;
    private final long accountIdTo;
    private final float workLoad;
    private final float wage;
    private final float workEnvironment;
    private final float competence;
    private final float pontuality;
    private final float assiduity;
    private final float demeanor;
    private final long version;

    public Rating(){
        ratingKey = null;
        accountIdFrom = 0;
        accountIdTo = 0;
        workLoad = 0;
        wage = 0;
        workEnvironment = 0;
        competence = 0;
        pontuality = 0;
        assiduity = 0;
        demeanor = 0;
        version = 0;
    }

    public Rating(long accountIDFrom,
                  long accountIDTo,
                  float workLoad,
                  float wage,
                  float workEnvironment,
                  float competence,
                  float pontuality,
                  float assiduity,
                  float demeanor,
                  long version
    ) {
        this.accountIdFrom = accountIDFrom;
        this.accountIdTo = accountIDTo;
        this.workLoad = workLoad;
        this.wage = wage;
        this.workEnvironment = workEnvironment;
        this.competence = competence;
        this.pontuality = pontuality;
        this.assiduity = assiduity;
        this.demeanor = demeanor;
        this.version = version;
        ratingKey = new RatingKey(accountIDFrom, accountIDTo);
    }

    public long getAccountIdFrom() {
        return accountIdFrom;
    }

    public long getAccountIdTo() {
        return accountIdTo;
    }

    public float getWorkLoad() {
        return workLoad;
    }

    public float getWage() {
        return wage;
    }

    public float getWorkEnvironment() {
        return workEnvironment;
    }

    public float getCompetence() {
        return competence;
    }

    public float getPontuality() {
        return pontuality;
    }

    public float getAssiduity() {
        return assiduity;
    }

    public float getDemeanor() {
        return demeanor;
    }

    @Override
    public RatingKey getIdentityKey() {
        return ratingKey;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public static class RatingKey {
        private final long accountIdFollower;
        private final long accountIdFollowed;

        public RatingKey(){
            accountIdFollower = 0;
            accountIdFollowed = 0;
        }

        public RatingKey(long accountIdFollowed, long accountIdFollower) {

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
