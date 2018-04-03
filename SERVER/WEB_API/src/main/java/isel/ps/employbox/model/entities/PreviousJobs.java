package isel.ps.employbox.model.entities;

import java.sql.Date;

public class PreviousJobs {
    private final long userId;
    private final long curriculumId;
    private final Date beginDate;
    private final Date endDate;
    private final long version;
    private final String companyName;
    private final String workLoad;
    private final String role;

    public PreviousJobs(long userId, long curriculumId, Date beginDate, Date endDate, String companyName, String workLoad, String role, long version) {
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this. companyName = companyName;
        this.workLoad = workLoad;
        this.role = role;
        this.endDate = endDate;
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    public long getUserId() {
        return userId;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getWorkLoad() {
        return workLoad;
    }

    public String getRole() {
        return role;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
