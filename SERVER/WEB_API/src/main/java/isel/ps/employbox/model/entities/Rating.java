package isel.ps.employbox.model.entities;


import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.EmbeddedId;

public class Rating implements DomainObject<Rating.RatingKey> {
    @EmbeddedId
    private final RatingKey ratingKey;

    private final long accountIdFrom;
    private final long accountIdTo;
    private final float workLoad;
    private final float wage;
    private final float workEnviroment;
    private final float competences;
    private final float ponctuality;
    private final float assiduity;
    private final float demeanor;
    private final long version;

    public Rating(){
        ratingKey = null;
        accountIdFrom = 0;
        accountIdTo = 0;
        workLoad = 0;
        wage = 0;
        workEnviroment = 0;
        competences = 0;
        ponctuality = 0;
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
        this.workEnviroment = workEnvironment;
        this.competences = competence;
        this.ponctuality = pontuality;
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

    public float getWorkEnviroment() {
        return workEnviroment;
    }

    public float getCompetences() {
        return competences;
    }

    public float getPonctuality() {
        return ponctuality;
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
        private final long accountIdFrom;
        private final long accountIdTo;

        public RatingKey(){
            accountIdFrom = 0;
            accountIdTo = 0;
        }

        public RatingKey(long accountIdFollowed, long accountIdFollower) {

            this.accountIdFrom = accountIdFollowed;
            this.accountIdTo = accountIdFollower;
        }

        public long getAccountIdFrom() {
            return accountIdFrom;
        }

        public long getAccountIdTo() {
            return accountIdTo;
        }
    }
}
