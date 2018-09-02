package isel.ps.employbox.model.input.curricula.childs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class InPreviousJobs {

    private long previousJobId;
    private long accountId;
    private long curriculumId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date beginDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;
    private String companyName;
    private String workLoad;
    private String role;
    private long version;

    public long getVersion() {
        return version;
    }

    public long getAccountId() {
        return accountId;
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

    public void setCurriculumId(long curriculumId){
        this.curriculumId = curriculumId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setWorkLoad(String workLoad) {
        this.workLoad = workLoad;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getPreviousJobId() {
        return previousJobId;
    }

    public void setPreviousJobId(long previousJobId) {
        this.previousJobId = previousJobId;
    }
}
