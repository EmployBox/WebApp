package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;

public class Project implements DomainObject<Project.ProjectKey> {
    private final long userId;
    private final long curriculumId;
    private final String name;
    private final String description;
    private final long version;
    private final ProjectKey key;

    public Project(){
        userId = 0;
        curriculumId = 0;
        name = null;
        description = null;
        version = 0;
        key = null;
    }

    public Project(long userId, long curriculumId, String name, String description, long version){
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.name = name;
        this.description = description;
        this.version = version;
        key = new ProjectKey(userId, curriculumId);
    }

    @Override
    public ProjectKey getIdentityKey() {
        return key;
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

    public static class ProjectKey {
        private final long userId;
        private final long curriculumId;

        public ProjectKey(){
            userId = 0;
            curriculumId = 0;
        }

        public ProjectKey(long userId, long curriculumId) {
            this.userId = userId;
            this.curriculumId = curriculumId;
        }

        public long getUserId() {
            return userId;
        }

        public long getCurriculumId() {
            return curriculumId;
        }
    }
}
