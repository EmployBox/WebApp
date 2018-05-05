package isel.ps.employbox.model.entities.CurriculumChilds;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;

public class Project implements DomainObject<Long>,CurriculumChild {

    @Id(isIdentity = true)
    private final long projectId;
    private long accountId;
    private long curriculumId;
    private final String name;
    private final String description;
    private final long version;


    public Project(){
        accountId = -1;
        curriculumId = -1;
        name = null;
        description = null;
        version = 0;
        projectId = -1;
    }

    public Project(long userId, long curriculumId, String name, String description, long version, long projectId){
        this.accountId = userId;
        this.curriculumId = curriculumId;
        this.name = name;
        this.description = description;
        this.version = version;
        this.projectId = projectId;
    }

    @Override
    public Long getIdentityKey() {
        return projectId;
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setCurriculumId(long curriculumId){
        this.curriculumId = curriculumId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

}
