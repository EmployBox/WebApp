package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.EmbeddedId;

import java.sql.Date;

public class AcademicBackground implements DomainObject<AcademicBackground.AcademicBackgroundKey> {

    @EmbeddedId
    private final AcademicBackgroundKey academicBackgroundKey;
    private final long accountId;
    private final long curriculumId;
    private final Date beginDate;
    private final Date endDate;
    private final String studyArea;
    private final String institution;
    private final String degreeObtained;
    private final long version;

    public AcademicBackground(){

        academicBackgroundKey = null;
        accountId = 0;
        curriculumId = 0;
        beginDate = null;
        endDate = null;
        studyArea = null;
        institution = null;
        degreeObtained = null;
        version = 0;
    }

    public AcademicBackground(long userId, long curriculumId, Date beginDate, Date endDate, String studyArea, String institution, String degreeObtained, long version) {
        this.accountId = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.studyArea = studyArea;
        this.institution = institution;
        this.degreeObtained = degreeObtained;
        this.version = version;
        academicBackgroundKey = new AcademicBackgroundKey(accountId, curriculumId);
    }

    @Override
    public AcademicBackgroundKey getIdentityKey() {
        return academicBackgroundKey;
    }

    public long getVersion() {
        return version;
    }

    public long getAccountId() {
        return accountId;
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

    public static class AcademicBackgroundKey {
            private final Long accountId;
            private final Long CurriculumId;

            public AcademicBackgroundKey(){
                accountId = null;
                CurriculumId = null;
            }

            public AcademicBackgroundKey(Long userId, Long curriculumId) {
                this.accountId = userId;
                CurriculumId = curriculumId;
            }

            public Long getAccountId() {
                return accountId;
            }

            public Long getCurriculumId() {
                return CurriculumId;
            }
    }
}
