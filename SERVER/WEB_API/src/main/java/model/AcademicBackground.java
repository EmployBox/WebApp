package model;

import java.sql.Date;


public class AcademicBackground extends DomainObject<String> {
    private final long accountID;
    private final long curriculumId;
    private final Date beginDate;
    private final Date endDate;
    private final String studyArea;
    private final String institution;
    private final String degreeObtained;
    private final long version;

    public AcademicBackground(long userId, long curriculumId, Date beginDate, Date endDate, String studyArea, String institution, String degreeObtained, long version) {
        super(String.format("AcademicBackgroundPK: %d %d",userId,curriculumId), version);
        this.accountID = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.studyArea = studyArea;
        this.institution = institution;
        this.degreeObtained = degreeObtained;
        this.version = version;
    }

    public static AcademicBackground create(long userId, long curriculumId, Date beginDate, Date endDate, String companyName, String workLoad, String role){
        AcademicBackground academicBackground = new AcademicBackground(userId, curriculumId, beginDate , endDate, companyName, workLoad, role, 0);
        academicBackground.markNew();
        return academicBackground;
    }

    public static AcademicBackground load(long userId, long curriculumId, Date beginDate, Date endDate, String companyName, String workLoad, String role, long version){
        AcademicBackground academicBackground = new AcademicBackground(userId, curriculumId, beginDate , endDate, companyName, workLoad, role, version);
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
