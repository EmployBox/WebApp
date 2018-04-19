package isel.ps.employbox.model.entities.composedKeys;

public class ApplicationKey {
    private long userId;
    private long jobId;

    public ApplicationKey(long userId, long jobId) {
        this.userId = userId;
        this.jobId = jobId;
    }

    public long getUserId() {
        return userId;
    }

    public long getJobId() {
        return jobId;
    }
}
