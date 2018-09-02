package isel.ps.employbox.model.input;

public class InRating {
    private long accountIdFrom;
    private long accountIdTo;
    private double workLoad;
    private double wage;
    private double workEnvironment;
    private double competence;
    private double pontuality;
    private double assiduity;
    private double demeanor;
    private long version;

    public long getAccountIdFrom() {
        return accountIdFrom;
    }

    public void setAccountIdFrom(long accountIDFrom) {
        this.accountIdFrom = accountIDFrom;
    }

    public long getAccountIdTo() {
        return accountIdTo;
    }

    public void setAccountIdTo(long accountIDTo) {
        this.accountIdTo = accountIDTo;
    }

    public double getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(double workLoad) {
        this.workLoad = workLoad;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public double getWorkEnvironment() {
        return workEnvironment;
    }

    public void setWorkEnvironment(double workEnvironment) {
        this.workEnvironment = workEnvironment;
    }

    public double getCompetence() {
        return competence;
    }

    public void setCompetence(double competence) {
        this.competence = competence;
    }

    public double getPontuality() {
        return pontuality;
    }

    public void setPontuality(double pontuality) {
        this.pontuality = pontuality;
    }

    public double getAssiduity() {
        return assiduity;
    }

    public void setAssiduity(double assiduity) {
        this.assiduity = assiduity;
    }

    public double getDemeanor() {
        return demeanor;
    }

    public void setDemeanor(double demeanor) {
        this.demeanor = demeanor;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
