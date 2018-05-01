package isel.ps.employbox.model.entities;

import org.github.isel.rapper.ColumnName;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class User extends Account {
    private final String name;
    private final String summary;
    private final String photoUrl;
    private final long version;


    @ColumnName(name = "userId")
    private final Supplier<List<Curriculum>> curricula;
    @ColumnName(name = "userId")
    private final Supplier<List<Application>> applications;

    public User(){
        name = null;
        summary = null;
        photoUrl = null;
        curricula = Collections::emptyList;
        applications = Collections::emptyList;
        version = 0;
    }

    public User(long accountID,
                String email,
                String password,
                float rating,
                long version,
                String name,
                String summary,
                String photoUrl,
                Supplier<List<Job>> offeredJobs,
                Supplier<List<Curriculum>> curricula,
                Supplier<List<Application>> applications,
                Supplier<List<Chat>> chats,
                Supplier<List<Comment>> comments,
                Supplier<List<Rating>> ratings,
                Supplier<List<User>> followers, long version1)
    {
        super(accountID, email, password, rating, version, offeredJobs, comments, chats, ratings, followers);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.curricula = curricula;
        this.applications = applications;
        this.version = version1;
    }

    public User(
            long accountId,
            String email,
            String password,
            float rating,
            String name,
            String summary, String photoUrl, long accountVersion, long version){
        super(accountId, email, password, rating, accountVersion);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.version = version;
        this.curricula = Collections::emptyList;
        this.applications = Collections::emptyList;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Supplier<List<Application>> getApplications() {
        return applications;
    }

    public Supplier<List<Curriculum>> getCurricula() {
        return curricula;
    }

}
