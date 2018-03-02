package model;

import java.sql.Date;

public class PreviousJobs extends DomainObject<String> {
    private final long userId;
    private final long curriculumId;
    private final Date beginDate;
    private final Date endDate;
    private final String companyName;
    private final String workLoad;
    private final String role;

    public PreviousJobs(long userId, long curriculumId, Date beginDate,Date endDate, String companyName, String workLoad, String role) {
        super(String.format("PreviousJobsPK: %d %d",userId,curriculumId));
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this. companyName = companyName;
        this.workLoad = workLoad;
        this.role = role;
        this.endDate = endDate;
    }

    public static PreviousJobs create(long userId, long curriculumId, Date beginDate, Date endDate, String companyName, String workLoad, String role){
        PreviousJobs previousJobs = new PreviousJobs(userId, curriculumId, beginDate , endDate,companyName, workLoad, role);
        previousJobs.markNew();
        return previousJobs;
    }

    public static PreviousJobs load(long userId, long curriculumId, Date beginDate, Date endDate, String companyName, String workLoad, String role){
        PreviousJobs previousJobs = new PreviousJobs(userId, curriculumId, beginDate , endDate, companyName, workLoad, role);
        previousJobs.markClean();
        return previousJobs;
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
