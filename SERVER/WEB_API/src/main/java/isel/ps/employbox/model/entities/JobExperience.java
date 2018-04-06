package isel.ps.employbox.model.entities;

public class JobExperience {
    private final long jobId;
    private final String competences;
    private final short years;
    private  long version;

    public JobExperience(long jobId, String competences, short years, long version){
        this.jobId = jobId;
        this.competences = competences;
        this.years = years;
        this.version = version;
    }

    public JobExperience(long jobId, String competences, short years){
        this.jobId = jobId;
        this.competences = competences;
        this.years = years;
    }

    public long getVersion() {
        return version;
    }

    public long getJobId() {
        return jobId;
    }

    public String getCompetences() {
        return competences;
    }

    public short getYears() {
        return years;
    }
}
