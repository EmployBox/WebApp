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

    public static User create(
            String email,
            String password,
            float rating,
            String name,
            String summary,
            String photoUrl
    ){
        User user = new User(
                defaultKey,
                email,
                password,
                rating,
                0,
                name,
                summary,
                photoUrl,
                Collections::emptyList,
                Collections::emptyList,
                Collections::emptyList,
                Collections::emptyList,
                Collections::emptyList,
                Collections::emptyList,
                Collections::emptyList
        );
        user.markNew();
        return user;
    }

    public static User load(
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
        User user = new User(
                accountID,
                email,
                password,
                rating,
                version,
                name,
                summary,
                photoUrl,
                offeredJobs,
                curriculums,
                applications,
                chats,
                comments,
                ratings,
                followers
        );
        user.markClean();
        return user;
    }

    public static User update(
            User user,
            String email,
            String password,
            float rating,
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
        user.markToBeDirty();
        User newUser = new User(user.accountID, email, password, rating, user.version, name, summary, photoUrl, offeredJobs, curriculums, applications, chats, comments, ratings, followers);
        newUser.markDirty();
        return newUser;
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
