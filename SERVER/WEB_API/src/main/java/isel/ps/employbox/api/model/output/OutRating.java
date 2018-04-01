package isel.ps.employbox.api.model.output;

public class OutRating {
    private final long accountIDFrom;
    private final long accountIDTo;
    private final float workLoad;
    private final float wage;
    private final float workEnvironment;
    private final float competence;
    private final float pontuality;
    private final float assiduity;
    private final float demeanor;


    public OutRating(long accountIDFrom, long accountIDTo, float workLoad, float wage, float workEnvironment, float competence, float pontuality, float assiduity, float demeanor) {
        this.accountIDFrom = accountIDFrom;
        this.accountIDTo = accountIDTo;
        this.workLoad = workLoad;
        this.wage = wage;
        this.workEnvironment = workEnvironment;
        this.competence = competence;
        this.pontuality = pontuality;
        this.assiduity = assiduity;
        this.demeanor = demeanor;
    }

    public long getAccountIDFrom() {
        return accountIDFrom;
    }

    public long getAccountIDTo() {
        return accountIDTo;
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
}
