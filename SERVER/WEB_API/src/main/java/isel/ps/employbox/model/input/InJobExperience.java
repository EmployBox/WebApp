package isel.ps.employbox.model.input;

public class InJobExperience {
    private long jobId;
    private String competences;
    private short years;

    public InJobExperience(){}

    public String getCompetences() {
        return competences;
    }

    public void setCompetences(String competences) {
        this.competences = competences;
    }

    public short getYears() {
        return years;
    }

    public void setYears(short years) {
        this.years = years;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }
}