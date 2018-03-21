package isel.ps.employbox.api.model.output;

import java.sql.Date;

public class OutApplication {
    private long userId;
    private long jobId;
    private long curriculumId;
    private Date date;

    public OutApplication(long userId, long jobId, long curriculumId, Date date){
        this.userId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
