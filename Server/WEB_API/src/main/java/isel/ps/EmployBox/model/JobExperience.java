package isel.ps.EmployBox.model;

public class JobExperience extends DomainObject<Long>{
    private final long jobId;
    private final String competeces;
    private final short years;

    public JobExperience(long jobId, String competeces, short years, long version){
        super(jobId,version);
        this.jobId = jobId;
        this.competeces = competeces;
        this.years = years;
    }

    public static JobExperience create(long jobId, String competeces, short years, long version) {
        JobExperience jobExperience = new JobExperience(jobId, competeces, years, version);
        jobExperience.markNew();
        return jobExperience;
    }

    public static JobExperience load(long jobId, String competences, short years, long version) {
        JobExperience jobExperience = new JobExperience(jobId, competences, years, version);
        jobExperience.markClean();
        return jobExperience;
    }

    public long getJobId() {
        return jobId;
    }

    public String getCompeteces() {
        return competeces;
    }

    public short getYears() {
        return years;
    }
}
