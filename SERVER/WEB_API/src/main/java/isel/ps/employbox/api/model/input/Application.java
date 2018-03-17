package isel.ps.employbox.api.model.input;

public class Application {

    private final long curriculumID;

    public Application(long curriculumID){

        this.curriculumID = curriculumID;
    }

    public long getCurriculumID() {
        return curriculumID;
    }
}
