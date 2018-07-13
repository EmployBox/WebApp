package isel.ps.employbox.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;
import com.github.jayield.rapper.Version;

public class JobExperience implements DomainObject<Long> {

    @Id( isIdentity = true)
    private final long jobExperienceId;
    private long jobId;
    private final String competences;
    private final int years;
    @Version
    private final long version;

    public JobExperience(){
        jobId = -1;
        years = 0;
        jobExperienceId = 0;
        competences = null;
        version = 0;
    }

    public JobExperience(long jobId, String competences,  short years){
        jobExperienceId = -1;
        this.competences = competences;
        this.years = years;
        this.jobId = jobId;
        version = 0;
    }

    public JobExperience(long jobExperienceId, long jobId, String competences, short years) {
        this.jobExperienceId = jobExperienceId;
        this.jobId = jobId;
        this.competences = competences;
        this.years = years;
        version = 0;
    }

    public JobExperience(long jobExperienceId, long jobId, String competences, short years, long version) {
        this.jobExperienceId = jobExperienceId;
        this.jobId = jobId;
        this.competences = competences;
        this.years = years;
        this.version = version;
    }

    @JsonIgnore
    @Override
    public Long getIdentityKey() {
        return jobExperienceId;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public int getYears() {
        return years;
    }

    public String getCompetences() {
        return competences;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId){
        this.jobId = jobId;
    }
}
