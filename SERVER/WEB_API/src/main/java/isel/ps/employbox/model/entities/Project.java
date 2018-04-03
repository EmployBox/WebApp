package isel.ps.employbox.model.entities;

public class Project {
    private final long userId;
    private final long curriculumId;
    private final String name;
    private final String description;
    private final long version;

    public Project(long userId, long curriculumId, String name, String description, long version){
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.name = name;
        this.description = description;
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
