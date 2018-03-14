package isel.ps.EmployBox.dal.domainModel;

import isel.ps.EmployBox.dal.util.Streamable;

public abstract class Account extends DomainObject<Long> {
    @ID(isIdentity = true)
    protected long accountID;
    protected final String email;
    protected final String password;
    protected final double rating;
       
    protected final Streamable<Job> offeredJobs;
    protected final Streamable<Comment> comments;
    protected final Streamable<User> following;
    protected final Streamable<Chat> chats;
    protected final Streamable<Rating> ratings;

    protected Account(
            long accountID,
            String email,
            String password,
            double rating,
            long version,
            Streamable<Job> offeredJobs,
            Streamable<Comment> comments,
            Streamable<Chat> chats,
            Streamable<Rating> ratings,
            Streamable<User> following
    ){
        super(accountID, version);
        this.accountID = accountID;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.offeredJobs = offeredJobs;
        this.chats = chats;
        this.ratings = ratings;
        this.comments = comments;
        this.following = following;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getRating() {
        return rating;
    }

    public Streamable<Job> getOfferedJobs() {
        return offeredJobs;
    }

    public Streamable<User> getFollowing() {
        return following;
    }

    public Streamable<Comment> getComments() {
        return comments;
    }

    public Streamable<Chat> getChats() {
        return chats;
    }

    public Streamable<Rating> getRatings() {
        return ratings;
    }
}
