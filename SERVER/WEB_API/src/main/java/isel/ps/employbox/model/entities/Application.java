package isel.ps.employbox.model.entities;

import java.sql.Date;

public class Application {
    private final long userId;
    private final long jobId;
    private final long curriculumId;
    private final Date date;
    private final long version;

    public Application(long userId, long jobId, long curriculumId, Date date, long version) {
        this.userId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this.version = version;
    }

    public Application(long userId, long jobId, long curriculumId, Date date) {
        this.userId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this.version = -1;
    }

    public long getVersion() {
        return version;
    }

    public Date getDate() {
        return date;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public long getJobId() {
        return jobId;
    }

    public long getUserId() {
        return userId;
    }
}
