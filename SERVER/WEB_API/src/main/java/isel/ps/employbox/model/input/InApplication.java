package isel.ps.employbox.model.input;

import java.sql.Timestamp;

public class InApplication {
    private long accountId;
    private long jobId;
    private long curriculumId;
    private Timestamp date;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
