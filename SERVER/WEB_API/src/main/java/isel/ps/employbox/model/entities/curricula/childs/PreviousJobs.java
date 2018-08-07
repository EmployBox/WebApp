package isel.ps.employbox.model.entities.curricula.childs;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;

import java.time.Instant;

public class PreviousJobs extends CurriculumChild implements DomainObject<Long> {

    @Id(isIdentity = true)
    private final long previousJobId;

    private long accountId;
    private long curriculumId;
    private final Instant beginDate;
    private final Instant endDate;
    private final String companyName;
    private final String workLoad;
    private final String role;
    @Version
    private final long version;


    public PreviousJobs(){
        accountId = 0;
        curriculumId = 0;
        beginDate = null;
        endDate = null;
        version = 0;
        companyName = null;
        workLoad = null;
        role = null;
        previousJobId = 0;
    }

    public PreviousJobs(
            long previousJobId,
            long userId,
            long curriculumId,
            Instant beginDate,
            Instant endDate,
            String companyName,
            String workLoad,
            String role,
            long version)
    {
        this.previousJobId = previousJobId;
        this.accountId = userId;
        this.curriculumId = curriculumId;
        this.beginDate = beginDate;
        this.companyName = companyName;
        this.workLoad = workLoad;
        this.role = role;
        this.endDate = endDate;
        this.version = version;
    }

    @Override
    public Long getIdentityKey() {
        return previousJobId;
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

    public String getCompanyName() {
        return companyName;
    }

    public String getWorkLoad() {
        return workLoad;
    }

    public String getRole() {
        return role;
    }

    public Instant getBeginDate() {
        return beginDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setCurriculumId(long curriculumId){
        this.curriculumId = curriculumId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
