package isel.ps.employbox.model.entities;

import isel.ps.employbox.model.entities.composedKeys.UserToUserKey;
import org.github.isel.rapper.DomainObject;

public class Rating implements DomainObject<UserToUserKey>{
    private final UserToUserKey ratingKey;
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
        ratingKey = new UserToUserKey(accountIDFrom, accountIDTo);
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
    public UserToUserKey getIdentityKey() {
        return ratingKey;
    }

    @Override
    public long getVersion() {
        return version;
    }
}
