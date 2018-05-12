package isel.ps.employbox.model.entities;


import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;
import com.github.jayield.rapper.Version;

import java.sql.Timestamp;

public class Application implements DomainObject<Long> {

    @Id(isIdentity = true)
    private final long applicationId;
    private final long accountId;
    private final long jobId;
    private final long curriculumId;
    private final Timestamp date;
    @Version
    private final long version;

    public Application(){
        accountId = 0;
        jobId = 0;
        curriculumId = 0;
        date = null;
        version = 0;
        applicationId = 0;
    }

    public Application(long applicationId, long userId, long jobId, long curriculumId, Timestamp date, long version) {
        this.applicationId = applicationId;
        this.accountId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this.version = version;
    }

    public Application(long userId, long jobId, long curriculumId, Timestamp date) {
        this.applicationId = -1;
        this.accountId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this.version = -1;
    }


    @Override
    public Long getIdentityKey() {
        return applicationId;
    }

    public long getVersion() {
        return version;
    }

    public Timestamp getDate() {
        return date;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public long getJobId() {
        return jobId;
    }

    public long getAccountId() {
        return accountId;
    }

}
