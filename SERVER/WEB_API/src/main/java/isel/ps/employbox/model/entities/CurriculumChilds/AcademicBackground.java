package isel.ps.employbox.model.entities.CurriculumChilds;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;
import com.github.jayield.rapper.Version;

import java.sql.Date;

public class AcademicBackground implements DomainObject<Long>,CurriculumChild {

    @Id(isIdentity = true)
    private final long academicBackgroundKey;

    private long accountId;
    private long curriculumId;
    private final Date beginDate;
    private final Date endDate;
    private final String studyArea;
    private final String institution;
    private final String degreeObtained;

    @Version
    private final long version;

    public AcademicBackground(){
        accountId = 0;
        curriculumId = 0;
        beginDate = null;
        endDate = null;
        studyArea = null;
        institution = null;
        degreeObtained = null;
        version = 0;
        academicBackgroundKey = 0;
    }

    public AcademicBackground(long userId, long curriculumId, Date beginDate, Date endDate, String studyArea, String institution, String degreeObtained, long version, Long academicBackgroundKey) {
        this.accountId = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.studyArea = studyArea;
        this.institution = institution;
        this.degreeObtained = degreeObtained;
        this.version = version;
        this.academicBackgroundKey = academicBackgroundKey;
    }

    @Override
    public Long getIdentityKey() {
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

    public void setCurriculumId(long curriculumId){
        this.curriculumId = curriculumId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
