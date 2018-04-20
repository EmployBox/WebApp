package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;

public class CurriculumExperience implements DomainObject<CurriculumExperience.CurriculumExperienceKey>{
    private final CurriculumExperienceKey curriculumExperienceKey;
    private final long userId;
    private final long curriculumId;
    private final String competences;
    private final short years;
    private final long version;

    public CurriculumExperience(){
        curriculumExperienceKey = null;
        userId = 0;
        curriculumId = 0;
        competences = null;
        years = 0;
        version = 0;
    }

    public CurriculumExperience(long userId, long curriculumId, String competences, short years, long version){
        this.userId = userId;
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
