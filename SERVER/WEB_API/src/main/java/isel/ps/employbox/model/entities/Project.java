package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.EmbeddedId;

public class Project implements DomainObject<Project.ProjectKey> {
    @EmbeddedId
    private final ProjectKey key;

    private final long accountId;
    private final long curriculumId;
    private final String name;
    private final String description;
    private final long version;


    public Project(){
        accountId = 0;
        curriculumId = 0;
        name = null;
        description = null;
        version = 0;
        key = null;
    }

    public Project(long userId, long curriculumId, String name, String description, long version){
        this.accountId = userId;
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

    public long getAccountId() {
        return accountId;
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
        private final long accountId;
        private final long curriculumId;

        public ProjectKey(){
            accountId = 0;
            curriculumId = 0;
        }

        public ProjectKey(long userId, long curriculumId) {
            this.accountId = userId;
            this.curriculumId = curriculumId;
        }

        public long getAccountId() {
            return accountId;
        }

        public long getCurriculumId() {
            return curriculumId;
        }
    }
}
