package isel.ps.employbox.model.entities;

import isel.ps.employbox.model.entities.composedKeys.ApplicationKey;
import org.github.isel.rapper.DomainObject;

import java.sql.Date;

public class Application implements DomainObject<ApplicationKey>{
    private final ApplicationKey applicationKey;
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
        applicationKey = new ApplicationKey(userId, jobId);
    }

    public Application(long userId, long jobId, long curriculumId, Date date) {
        this.userId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this.version = -1;
        applicationKey = new ApplicationKey(userId, jobId);
    }

    @Override
    public ApplicationKey getIdentityKey() {
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
}
