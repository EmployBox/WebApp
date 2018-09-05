package isel.ps.employbox.model.input;

public class InJobExperience {
    private long jobExperienceId;
    private long jobId;
    private String competence;
    private short years;
    private long version;

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
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

    public long getJobExperienceId() {
        return jobExperienceId;
    }

    public void setJobExperienceId(long jobExperienceId) {
        this.jobExperienceId = jobExperienceId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}