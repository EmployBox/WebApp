package model;

public class CurriculumExperience extends DomainObject<String> {
    private final long userId;
    private final long curriculumId;
    private final long experienceId;

    private CurriculumExperience(long userId, long curriculumId, long experienceId, long version){
        super(String.format("CEPK: %s %s %s",userId,curriculumId,experienceId), version);
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.experienceId = experienceId;
    }

    public static CurriculumExperience create(long userId,
                                              long curriculumId,
                                              long experienceId,
                                              long version
    )
    {
        CurriculumExperience curriculumExperience = new CurriculumExperience( userId, curriculumId, experienceId, version);
        curriculumExperience.markNew();
        return curriculumExperience;
    }

    public static CurriculumExperience load(long userId,
                                              long curriculumId,
                                              long experienceId,
                                              long version
    )
    {
        CurriculumExperience curriculumExperience = new CurriculumExperience( userId, curriculumId, experienceId, version);
        curriculumExperience.markClean();
        return curriculumExperience;
    }

    public long getUserId() {
        return userId;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public long getExperienceId() {
        return experienceId;
    }
}
