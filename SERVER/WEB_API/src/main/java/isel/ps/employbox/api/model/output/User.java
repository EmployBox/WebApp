package isel.ps.employbox.api.model.output;

public class User {
    private final long id;
    private final String name;
    private final String email;
    private final String photo_url;
    private final String summary;
    private final double rating;
    private final String offeredJobs_url;
    private final String curriculums_url;
    private final String applications_url;
    private final String chats_url;
    private final String comments_url;
    private final String ratings_url;
    private final String followers_url;
    private final String following_url;

    public User(long id, String name, String email, String photo_url, String summary, double rating) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photo_url = photo_url;
        this.summary = summary;
        this.rating = rating;
        offeredJobs_url = String.format("/account/%d/job", id);
        curriculums_url = String.format("/account/user/%d/curriculums", id);
        applications_url = String.format("/account/user/%d/applications", id);
        chats_url = String.format("/account/%d/chats", id);
        comments_url = String.format("/account/%d/comments", id);
        ratings_url = String.format("/account/%d/ratings", id);
        followers_url = String.format("/account/%d/followers", id);
        following_url = String.format("/account/%d/following", id);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getSummary() {
        return summary;
    }

    public double getRating() {
        return rating;
    }

    public String getOfferedJobs_url() {
        return offeredJobs_url;
    }

    public String getCurriculums_url() {
        return curriculums_url;
    }

    public String getApplications_url() {
        return applications_url;
    }

    public String getChats_url() {
        return chats_url;
    }

    public String getComments_url() {
        return comments_url;
    }

    public String getRatings_url() {
        return ratings_url;
    }

    public String getFollowers_url() {
        return followers_url;
    }

    public String getFollowing_url() {
        return following_url;
    }
}
