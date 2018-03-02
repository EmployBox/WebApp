package model;

import java.sql.Date;

public class AcademicBackground extends DomainObject<String> {
    private final long accountID;
    private final long curriculumId;
    private Date beginDate;
    private Date endDate;
    private String studyArea;
    private String institution;
    private String degreeObtained;

    public AcademicBackground(long userId, long curriculumId, Date beginDate, String studyArea, String institution, String degreeObtained) {
        super(String.format("AcademicBackgroundPK: %d %d",userId,curriculumId));
        this.accountID = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this.studyArea = studyArea;
        this.institution = institution;
        this.degreeObtained = degreeObtained;
    }

    public static AcademicBackground create(long userId, long curriculumId, Date beginDate, String companyName, String workLoad, String role){
        AcademicBackground academicBackground = new AcademicBackground(userId, curriculumId, beginDate , companyName, workLoad, role);
        academicBackground.markNew();
        return academicBackground;
    }

    public static AcademicBackground load(long userId, long curriculumId, Date beginDate, String companyName, String workLoad, String role){
        AcademicBackground academicBackground = new AcademicBackground(userId, curriculumId, beginDate , companyName, workLoad, role);
        academicBackground.markClean();
        return academicBackground;
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

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStudyArea() {
        return studyArea;
    }

    public void setStudyArea(String studyArea) {
        this.studyArea = studyArea;
    }

    public String getDegreeObtained() {
        return degreeObtained;
    }

    public void setDegreeObtained(String degreeObtained) {
        this.degreeObtained = degreeObtained;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
