package model;

import util.Streamable;

import java.util.stream.Stream;

public class User extends Account {
    private final String name;
    private final String summary;
    private final String photoUrl;

    private final Streamable<Curriculum> curriculums;
    private final Streamable<Application> applications;

    public User(long accountID,
                String email,
                String password,
                double rating,
                long version,
                String name,
                String summary,
                String photoUrl,
                Streamable<Curriculum> curriculums,
                Streamable<Application> applications
    ){
        super(accountID,email,password,rating,version);
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
                            long version,
                            String name,
                            String summary,
                            String photoUrl,
                            Streamable<Curriculum> curriculums,
                            Streamable<Application> applications
    ){
        User user = new User(-1 ,email,password,rating,version,name,summary,photoUrl, curriculums, applications);
        user.markNew();
        return user;
    }

    public static User load(long accountID,
                            String email,
                            String password,
                            double rating,
                            long version,
                            String name,
                            String summary,
                            String photoUrl,
                            Streamable<Curriculum> curriculums,
                            Streamable<Application> applications
    ){
        User user = new User(accountID, email, password, rating, version, name, summary, photoUrl, curriculums, applications);
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

    public Stream<Curriculum> getExperiences(){
        return this.curriculums.get();
    }

    public Stream<Application> getApplications() {
        return applications.get();
    }

}
