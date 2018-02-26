package model;

public class Experience extends DomainObject {

    private final String competence;
    private final short years;

    private Experience(Object identityKey, String competence, short years) {
        super(identityKey);
        this.competence = competence;
        this.years = years;
    }

    public static Experience create(Object identityKey, String competence, short years){
        Experience experience = new Experience(identityKey, competence, years);
        experience.markNew();
        return experience;
    }

    public static Experience load(Object identityKey, String competence, short years){
        Experience experience = new Experience(identityKey, competence, years);
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
