package isel.ps.employbox.dal.model;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class User extends Account {
    private final String name;
    private final String summary;
    private final String photoUrl;

    private final Supplier<List<Curriculum>> curriculums;
    private final Supplier<List<Application>> applications;

    public User(
            long accountID,
            String email,
            String password,
            float rating,
            long version,
            String name,
            String summary,
            String photoUrl,
            Supplier<List<Job>> offeredJobs,
            Supplier<List<Curriculum>> curriculums,
            Supplier<List<Application>> applications,
            Supplier<List<Chat>> chats,
            Supplier<List<Comment>> comments,
            Supplier<List<Rating>> ratings,
            Supplier<List<User>> followers
    ){
        super(accountID, email, password, rating, version, offeredJobs, comments, chats, ratings, followers);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.curriculums = curriculums;
        this.applications = applications;
    }

    public User(String email, String password, float rating, String name, String summary, String photoUrl){
        super(email, password, rating);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.curriculums = Collections::emptyList;
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

    public Supplier<List<Curriculum>> getCurriculums() {
        return curriculums;
    }
}
