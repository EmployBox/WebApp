package isel.ps.employbox.model.input;

public class InRating {
    private long accountIDFrom;
    private long accountIDTo;
    private float workLoad;
    private float wage;
    private float workEnvironment;
    private float competence;
    private float pontuality;
    private float assiduity;
    private float demeanor;

    public long getAccountIDFrom() {
        return accountIDFrom;
    }

    public void setAccountIDFrom(long accountIDFrom) {
        this.accountIDFrom = accountIDFrom;
    }

    public long getAccountIDTo() {
        return accountIDTo;
    }

    public void setAccountIDTo(long accountIDTo) {
        this.accountIDTo = accountIDTo;
    }

    public float getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(float workLoad) {
        this.workLoad = workLoad;
    }

    public float getWage() {
        return wage;
    }

    public void setWage(float wage) {
        this.wage = wage;
    }

    public float getWorkEnvironment() {
        return workEnvironment;
    }

    public void setWorkEnvironment(float workEnvironment) {
        this.workEnvironment = workEnvironment;
    }

    public float getCompetence() {
        return competence;
    }

    public void setCompetence(float competence) {
        this.competence = competence;
    }

    public float getPontuality() {
        return pontuality;
    }

    public void setPontuality(float pontuality) {
        this.pontuality = pontuality;
    }

    public float getAssiduity() {
        return assiduity;
    }

    public void setAssiduity(float assiduity) {
        this.assiduity = assiduity;
    }

    public float getDemeanor() {
        return demeanor;
    }

    public void setDemeanor(float demeanor) {
        this.demeanor = demeanor;
    }
}
