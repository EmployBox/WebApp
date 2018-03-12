package isel.ps.EmployBox.model;

import isel.ps.EmployBox.util.Streamable;

import java.util.stream.Stream;

public class User extends Account {
    private final String name;
    private final String summary;
    private final String photoUrl;

    private final Streamable<Curriculum> curriculums;
    private final Streamable<Application> applications;

    private User(
            long accountID,
            String email,
            String password,
            double rating,
            long version,
            String name,
            String summary,
            String photoUrl,
            Streamable<Job> offeredJobs,
            Streamable<Curriculum> curriculums,
            Streamable<Application> applications,
            Streamable<Chat>chats,
            Streamable<Comment> comments,
            Streamable<Rating> ratings,
            Streamable<User> followers
    ){
        super(accountID, email, password, rating, version, offeredJobs, comments, chats, ratings, followers);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.curriculums = curriculums;
        this.applications = applications;
    }

    public static User create(
            String email,
            String password,
            double rating,
            String name,
            String summary,
            String photoUrl
    ){
        Streamable empty = Stream::empty;
        User user = new User(
                defaultKey,
                email,
                password,
                rating,
                0,
                name,
                summary,
                photoUrl,
                empty,
                empty,
                empty,
                empty,
                empty,
                empty,
                empty
        );
        user.markNew();
        return user;
    }

    public static User load(
            long accountID,
            String email,
            String password,
            double rating,
            long version,
            String name,
            String summary,
            String photoUrl,
            Streamable<Job> offeredJobs,
            Streamable<Curriculum> curriculums,
            Streamable<Application> applications,
            Streamable<Chat>chats,
            Streamable<Comment> comments,
            Streamable<Rating> ratings,
            Streamable<User> followers
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

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Streamable<Application> getApplications() {
        return applications;
    }

    public Streamable<Curriculum> getCurriculums() {
        return curriculums;
    }
}
