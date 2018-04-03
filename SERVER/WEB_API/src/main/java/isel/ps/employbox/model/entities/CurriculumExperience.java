package isel.ps.employbox.model.entities;

public class CurriculumExperience {
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
