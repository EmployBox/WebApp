package isel.ps.employbox.model.entities;

import isel.ps.employbox.model.entities.composedKeys.UserCurriculumKey;
import org.github.isel.rapper.DomainObject;

public class CurriculumExperience implements DomainObject<UserCurriculumKey>{
    private final UserCurriculumKey curriculumExperienceKey;
    private final long userId;
    private final long curriculumId;
    private final String competences;
    private final short years;
    private final long version;

    public CurriculumExperience(long userId, long curriculumId, String competences, short years, long version){
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.competences = competences;
        this.years = years;
        this.version = version;
        curriculumExperienceKey = new UserCurriculumKey(userId, curriculumId);
    }

    @Override
    public UserCurriculumKey getIdentityKey() {
        return curriculumExperienceKey;
    }

    public long getVersion() {
        return version;
    }

    public long getUserId() {
        return userId;
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
}
