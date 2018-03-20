package isel.ps.employbox.api.model.input;

public class InApplication {

    private final long curriculumID;

    public InApplication(long curriculumID){

        this.curriculumID = curriculumID;
    }

    public long getCurriculumID() {
        return curriculumID;
    }
}
