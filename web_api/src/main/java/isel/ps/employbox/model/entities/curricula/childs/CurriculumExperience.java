package isel.ps.employbox.model.entities.curricula.childs;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;

public class CurriculumExperience extends CurriculumChild implements DomainObject<Long> {

    @Id(isIdentity = true)
    private final long curriculumExperienceId;

    private long accountId;
    private long curriculumId;
    private final String competence;
    private final int years;

    @Version
    private final long version;


    public CurriculumExperience(){
        accountId = 0;
        curriculumId = 0;
        competence = null;
        years = 0;
        version = 0;
        curriculumExperienceId = 0;
    }

    public CurriculumExperience(long userId, long curriculumId, String competences, int years, long version, Long curriculumExperienceId){
        this.accountId = userId;
        this.curriculumId = curriculumId;
        this.competence = competences;
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

    public String getCompetence() {
        return competence;
    }

    public int getYears() {
        return years;
    }

    public void setCurriculumId(long curriculumId){
        this.curriculumId = curriculumId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
