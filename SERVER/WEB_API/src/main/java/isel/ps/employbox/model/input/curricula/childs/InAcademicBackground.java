package isel.ps.employbox.model.input.curricula.childs;

import java.sql.Timestamp;

public class InAcademicBackground {
    private long accountId;
    private long curriculumId;
    private Timestamp beginDate;
    private Timestamp endDate;
    private String studyArea;
    private String institution;
    private String degreeObtained;
    private long version;
    private long id;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public void setCurriculumId(long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getStudyArea() {
        return studyArea;
    }

    public void setStudyArea(String studyArea) {
        this.studyArea = studyArea;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDegreeObtained() {
        return degreeObtained;
    }

    public void setDegreeObtained(String degreeObtained) {
        this.degreeObtained = degreeObtained;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
