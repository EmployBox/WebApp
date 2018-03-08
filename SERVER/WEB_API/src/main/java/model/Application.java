package model;

import java.sql.Date;

public class Application extends DomainObject<String> {
    @ID
    private final long userId;
    @ID
    private final long jobId;
    private final long curriculumId;
    private final Date date;

    private Application(long userId, long jobId,long curriculumId, Date date, long version) {
        super(String.format("ApplicationPK %d %d",userId,jobId), version);
        this.userId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
    }

    public static Application create(long userId, long jobId,long curriculumId, Date date){
        Application application = new Application(userId,jobId,curriculumId, date, 0);
        application.markNew();
        return application;
    }

    public static Application load(long userId, long jobId, long curriculumId, Date date, long version){
        Application application = new Application(userId,jobId,curriculumId, date, version);
        application.markClean();
        return application;
    }

    public static Application update(Application application, long curriculumId){
        application.markToBeDirty();
        Application newApplication = new Application(application.getUserId(), application.getJobId(), curriculumId, application.getDate(), application.getNextVersion());
        newApplication.markDirty();
        return newApplication;
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
