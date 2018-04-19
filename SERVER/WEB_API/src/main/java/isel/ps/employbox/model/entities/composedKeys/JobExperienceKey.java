package isel.ps.employbox.model.entities.composedKeys;

public class JobExperienceKey {
    private final long jobId;
    private final String competences;

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
