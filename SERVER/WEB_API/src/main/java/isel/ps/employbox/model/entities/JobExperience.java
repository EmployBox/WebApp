package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.EmbeddedId;

public class JobExperience implements DomainObject<JobExperience.JobExperienceKey> {

    private final short years;
    private long version;

    @EmbeddedId
    private final JobExperienceKey jobExperienceKey;

    public JobExperience(){
        years = 0;
        jobExperienceKey = null;
    }

    public JobExperience(long jobId, String competences, short years, long version){
        this.years = years;
        this.version = version;
        jobExperienceKey = new JobExperienceKey(jobId, competences);
    }

    public JobExperience(long jobId, String competences, short years){
        this.years = years;
        jobExperienceKey = new JobExperienceKey(jobId, competences);
    }

    @Override
    public JobExperienceKey getIdentityKey() {
        return jobExperienceKey;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public short getYears() {
        return years;
    }

    public static class JobExperienceKey {
        private long jobId;
        private final String competences;

        public JobExperienceKey(){
            jobId = 0;
            competences = null;
        }

        public JobExperienceKey(long jobId, String competences) {
            this.jobId = jobId;
            this.competences = competences;
        }

        public long getJobId() {
            return jobId;
        }

        public String getCompetences() {
            return competences;
        }

        public void setJobId(long jobId){
            this.jobId = jobId;
        }
    }
}
