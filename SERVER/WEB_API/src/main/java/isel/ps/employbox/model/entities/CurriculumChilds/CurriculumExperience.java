package isel.ps.employbox.model.entities.CurriculumChilds;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;

public class CurriculumExperience implements DomainObject<Long>,CurriculumChild {

    @Id(isIdentity = true)
    private final long curriculumExperienceId;

    private long accountId;
    private long curriculumId;
    private final String competences;
    private final short years;
    private final long version;


    public CurriculumExperience(){
        accountId = 0;
        curriculumId = 0;
        competences = null;
        years = 0;
        version = 0;
        curriculumExperienceId = 0;
    }

    public CurriculumExperience(long userId, long curriculumId, String competences, short years, long version, Long curriculumExperienceId){
        this.accountId = userId;
        this.curriculumId = curriculumId;
        this.competences = competences;
        this.years = years;
        this.version = version;
        this.curriculumExperienceId = curriculumExperienceId;
    }

    @Override
    public Long getIdentityKey() {
        return curriculumExperienceId;
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

    public String getCompetences() {
        return competences;
    }

    public short getYears() {
        return years;
    }

    public void setCurriculumId(long curriculumId){
        this.curriculumId = curriculumId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
