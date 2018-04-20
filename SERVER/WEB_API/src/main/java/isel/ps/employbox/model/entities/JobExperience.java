package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;

public class JobExperience implements DomainObject<JobExperience.JobExperienceKey> {
    private final long jobId;
    private final String competences;
    private final short years;
    private long version;
    private final JobExperienceKey jobExperienceKey;

    public JobExperience(){
        jobId = 0;
        competences = null;
        years = 0;
        jobExperienceKey = null;
    }

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

    public static class JobExperienceKey {
        private final long jobId;
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
    }
}
