package isel.ps.EmployBox.model;
//TODO acho q nao esta de acordo com a db
public class CurriculumExperience extends DomainObject<String> {
    private final long userId;
    private final long curriculumId;
    private final String competences;
    private final short years;

    public CurriculumExperience(long userId, long curriculumId, String competences, short years, long version){
        super(String.format("%d %d",userId,curriculumId),version);
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.competences = competences;
        this.years = years;
    }

    public static CurriculumExperience create(long userId,
                                              long curriculumId,
                                              String competences,
                                              short years,
                                              long version
    )
    {
        CurriculumExperience curriculumExperience = new CurriculumExperience( userId, curriculumId, competences, years, version);
        curriculumExperience.markNew();
        return curriculumExperience;
    }

    public static CurriculumExperience load(
                                        long userId,
                                        long curriculumId,
                                        String competences,
                                        short years,
                                        long version
    )
    {
        CurriculumExperience curriculumExperience = new CurriculumExperience( userId, curriculumId, competences, years, version);
        curriculumExperience.markClean();
        return curriculumExperience;
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
