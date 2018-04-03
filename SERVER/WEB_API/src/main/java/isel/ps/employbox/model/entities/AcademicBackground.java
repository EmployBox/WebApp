package isel.ps.employbox.model.entities;

import java.sql.Date;

public class AcademicBackground {
    private final long accountID;
    private final long curriculumId;
    private final Date beginDate;
    private final Date endDate;
    private final String studyArea;
    private final String institution;
    private final String degreeObtained;
    private final long version;

    public AcademicBackground(long userId, long curriculumId, Date beginDate, Date endDate, String studyArea, String institution, String degreeObtained, long version) {
        this.accountID = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.studyArea = studyArea;
        this.institution = institution;
        this.degreeObtained = degreeObtained;
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    public long getAccountID() {
        return accountID;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStudyArea() {
        return studyArea;
    }

    public String getDegreeObtained() {
        return degreeObtained;
    }

    public String getInstitution() {
        return institution;
    }
}
