package isel.ps.EmployBox.model;

public class Project extends DomainObject<String> {
    @ID
    private final long userId;
    @ID
    private final long curriculumId;
    private final String name;
    private final String description;

    private Project(long userId, long curriculumId, String name, String description, long version){
        super(String.format("%d %d",userId,curriculumId), version);
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.name = name;
        this.description = description;
    }

    public static Project create(long userId, long curriculumId, String name, String description){
        Project project = new Project(userId,curriculumId,name, description, 0);
        project.markNew();
        return project;
    }

    public static Project load(long userId, long curriculumId, String name, String description, long version){
        Project project = new Project(userId,curriculumId,name, description, version);
        project.markClean();
        return project;
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