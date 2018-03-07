package model;

public class JobExperience extends DomainObject<Long>{
    private final long jobId;

    private JobExperience(long jobId, long version){
        super(jobId,version);
        this.jobId = jobId;
    }

    public static JobExperience create(long jobId, long version) {
        JobExperience jobExperience = new JobExperience(jobId,version);
        jobExperience.markNew();
        return jobExperience;
    }

    public static JobExperience load(long jobId, long version) {
        JobExperience jobExperience = new JobExperience(jobId,version);
        jobExperience.markClean();
        return jobExperience;
    }

    public long getJobId() {
        return jobId;
    }
}
