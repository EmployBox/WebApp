package isel.ps.EmployBox.dal.model;

import java.sql.Date;


public class AcademicBackground extends DomainObject<String> {
    @ID
    private final long accountID;
    @ID
    private final long curriculumId;
    private final Date beginDate;
    private final Date endDate;
    private final String studyArea;
    private final String institution;
    private final String degreeObtained;

    public AcademicBackground(long userId, long curriculumId, Date beginDate, Date endDate, String studyArea, String institution, String degreeObtained, long version) {
        super(String.format("%d-%d", userId, curriculumId), version);
        this.accountID = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.studyArea = studyArea;
        this.institution = institution;
        this.degreeObtained = degreeObtained;
    }

    public static AcademicBackground create(long userId, long curriculumId, Date beginDate, Date endDate, String studyArea, String institution, String degreeObtained){
        AcademicBackground academicBackground = new AcademicBackground(userId, curriculumId, beginDate , endDate, studyArea, institution, degreeObtained, 0);
        academicBackground.markNew();
        return academicBackground;
    }

    public static AcademicBackground load(long userId, long curriculumId, Date beginDate, Date endDate, String studyArea, String institution, String degreeObtained, long version){
        AcademicBackground academicBackground = new AcademicBackground(userId, curriculumId, beginDate , endDate, studyArea, institution, degreeObtained, version);
        academicBackground.markClean();
        return academicBackground;
    }

    public static AcademicBackground update(AcademicBackground academicBackground, Date beginDate, Date endDate, String studyArea, String institution, String degreeObtained){
        academicBackground.markToBeDirty();
        AcademicBackground newAcademic = new AcademicBackground(
                academicBackground.getAccountID(),
                academicBackground.getCurriculumId(),
                beginDate ,
                endDate,
                studyArea,
                institution,
                degreeObtained,
                academicBackground.getNextVersion()
        );
        newAcademic.markDirty();
        return newAcademic;
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
