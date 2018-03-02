package model;

public class Experience extends DomainObject<Long> {

    private final String competence;
    private final short years;

    private Experience(long experienceID, String competence, short years) {
        super(experienceID);
        this.competence = competence;
        this.years = years;
    }

    public static Experience create( String competence, short years){
        Experience experience = new Experience(-1, competence, years);
        experience.markNew();
        return experience;
    }

    public static Experience load( long experienceId, String competence, short years){
        Experience experience = new Experience(experienceId, competence, years);
        experience.markClean();
        return experience;
    }

    public String getCompetence() {
        return competence;
    }

    public short getYears() {
        return years;
    }
}
