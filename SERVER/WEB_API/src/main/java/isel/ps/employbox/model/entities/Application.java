package isel.ps.employbox.model.entities;


import org.github.isel.rapper.DomainObject;

import java.sql.Date;

public class Application implements DomainObject<Application.ApplicationKeys> {
    private final ApplicationKeys applicationKey;
    private final long userId;
    private final long jobId;
    private final long curriculumId;
    private final Date date;
    private final long version;

    public Application(){
        applicationKey = null;
        userId = 0;
        jobId = 0;
        curriculumId = 0;
        date = null;
        version = 0;
    }

    public Application(long userId, long jobId, long curriculumId, Date date, long version) {
        this.userId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this.version = version;
        applicationKey = new ApplicationKeys(userId, jobId);
    }

    public Application(long userId, long jobId, long curriculumId, Date date) {
        this.userId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this.version = -1;
        applicationKey = new ApplicationKeys(userId, jobId);
    }

    @Override
    public ApplicationKeys getIdentityKey() {
        return applicationKey;
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

    public static class ApplicationKeys {
        private long userId;
        private long jobId;

        public ApplicationKeys(){ }

        public ApplicationKeys(long userId, long jobId) {
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
}
