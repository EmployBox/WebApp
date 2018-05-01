package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;
import org.github.isel.rapper.EmbeddedId;

public class CurriculumExperience implements DomainObject<CurriculumExperience.CurriculumExperienceKey>{

    @EmbeddedId
    private final CurriculumExperienceKey curriculumExperienceKey;

    private final long accountId;
    private final long curriculumId;
    private final String competences;
    private final short years;
    private final long version;

    public CurriculumExperience(){
        curriculumExperienceKey = null;
        accountId = 0;
        curriculumId = 0;
        competences = null;
        years = 0;
        version = 0;
    }

    public CurriculumExperience(long userId, long curriculumId, String competences, short years, long version){
        this.accountId = userId;
        this.curriculumId = curriculumId;
        this.competences = competences;
        this.years = years;
        this.version = version;
        curriculumExperienceKey = new CurriculumExperienceKey(userId, curriculumId);
    }

    @Override
    public CurriculumExperienceKey getIdentityKey() {
        return curriculumExperienceKey;
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

    public static class CurriculumExperienceKey {
        private final long userId;
        private final long curriculumId;

        public CurriculumExperienceKey(){
            userId = 0;
            curriculumId = 0;
        }

        public CurriculumExperienceKey(long userId, long curriculumId) {
            this.userId = userId;
            this.curriculumId = curriculumId;
        }

        public long getUserId() {
            return userId;
        }

        public long getCurriculumId() {
            return curriculumId;
        }
    }
}
