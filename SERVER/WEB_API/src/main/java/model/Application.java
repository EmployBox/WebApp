package model;

import java.sql.Date;

public class Application extends DomainObject {
    private long userId;
    private long jobId;
    private long curriculumId;
    private Date date;

    public Application(long userId, long jobId,long curriculumId, Date date) {
        super(String.format("ApplicationPK %s %s",userId,jobId));
        this.userId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
    }

    public static Application create(long userId, long jobId,long curriculumId, Date date){
        Application application = new Application(userId,jobId,curriculumId, date);
        application.markNew();
        return application;
    }

    public static Application load(long userId, long jobId,long curriculumId, Date date){
        Application application = new Application(userId,jobId,curriculumId, date);
        application.markClean();
        return application;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
