package model;

public class Project extends DomainObject<String> {
    private final long userId;
    private final long curriculumId;
    private final String name;
    private final String description;

    public Project(long userId, long curriculumId, String name, String description){
        super(String.format("ProjectPK: %d %d",userId,curriculumId));
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.name = name;
        this.description = description;
    }

    public static Project create(long userId, long curriculumId, String name, String description){
        Project project = new Project(userId,curriculumId,name, description);
        project.markNew();
        return project;
    }

    public static Project load(long userId, long curriculumId, String name, String description){
        Project project = new Project(userId,curriculumId,name, description);
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
