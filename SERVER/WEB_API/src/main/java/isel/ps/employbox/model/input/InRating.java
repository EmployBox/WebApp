package isel.ps.employbox.model.input;

public class InRating {
    private long accountIdFrom;
    private long accountIdDest;
    private double workLoad;
    private double wage;
    private double workEnvironment;
    private double competences;
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

    public long getAccountIdDest() {
        return accountIdDest;
    }

    public void setAccountIdDest(long accountIDTo) {
        this.accountIdDest = accountIDTo;
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

    public double getCompetences() {
        return competences;
    }

    public void setCompetences(double competences) {
        this.competences = competences;
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
