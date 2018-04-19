package isel.ps.employbox.model.entities;

import isel.ps.employbox.model.entities.composedKeys.JobExperienceKey;
import org.github.isel.rapper.DomainObject;

public class JobExperience implements DomainObject<JobExperienceKey> {
    private final long jobId;
    private final String competences;
    private final short years;
    private long version;
    private final JobExperienceKey jobExperienceKey;

    public JobExperience(long jobId, String competences, short years, long version){
        this.jobId = jobId;
        this.competences = competences;
        this.years = years;
        this.version = version;
        jobExperienceKey = new JobExperienceKey(jobId, competences);
    }

    public JobExperience(long jobId, String competences, short years){
        this.jobId = jobId;
        this.competences = competences;
        this.years = years;
        jobExperienceKey = new JobExperienceKey(jobId, competences);
    }

    @Override
    public JobExperienceKey getIdentityKey() {
        return jobExperienceKey;
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
