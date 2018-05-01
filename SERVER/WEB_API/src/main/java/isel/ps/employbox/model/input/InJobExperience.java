package isel.ps.employbox.model.input;

public class InJobExperience {
    private String competence;
    private short years;

    public InJobExperience(){}

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public short getYears() {
        return years;
    }

    public void setYears(short years) {
        this.years = years;
    }
}