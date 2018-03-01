package model;

import java.sql.Date;

public class PreviousJobs extends DomainObject {
    private final long userId;
    private final long curriculumId;
    private Date beginDate;
    private Date endDate;
    private String companyName;
    private String workLoad;
    private String role;

    public PreviousJobs(long userId, long curriculumId, Date beginDate, String companyName, String workLoad, String role) {
        super(String.format("PreviousJobsPK: ? ?",userId,curriculumId));
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this. companyName = companyName;
        if(workLoad != "total" || workLoad != "partial")
            this.workLoad = "total";
        else
            this.workLoad = workLoad;
        this.role = role;
    }

    public static PreviousJobs create(long userId, long curriculumId, Date beginDate, String companyName, String workLoad, String role){
        PreviousJobs previousJobs = new PreviousJobs(userId, curriculumId, beginDate , companyName, workLoad, role);
        previousJobs.markNew();
        return previousJobs;
    }

    public static PreviousJobs load(long userId, long curriculumId, Date beginDate, String companyName, String workLoad, String role){
        PreviousJobs previousJobs = new PreviousJobs(userId, curriculumId, beginDate , companyName, workLoad, role);
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

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(String workLoad) {
        this.workLoad = workLoad;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
