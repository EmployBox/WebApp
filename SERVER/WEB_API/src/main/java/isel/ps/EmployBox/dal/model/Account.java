package isel.ps.EmployBox.dal.model;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class Account extends DomainObject<Long> {
    @ID(isIdentity = true)
    protected long accountID;
    protected final String email;
    protected final String password;
    protected final float rating;
       
    protected final Supplier<List<Job>> offeredJobs;
    protected final Supplier<List<Comment>> comments;
    protected final Supplier<List<User>> following;
    protected final Supplier<List<Chat>> chats;
    protected final Supplier<List<Rating>> ratings;

    protected Account(
            long accountID,
            String email,
            String password,
            float rating,
            long version,
            Supplier<List<Job>> offeredJobs,
            Supplier<List<Comment>> comments,
            Supplier<List<Chat>> chats,
            Supplier<List<Rating>> ratings,
            Supplier<List<User>> following
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

    public float getRating() {
        return rating;
    }

    public Stream<Job> getOfferedJobs() {
        return offeredJobs.get().stream();
    }

    public Stream<User> getFollowing() {
        return following.get().stream();
    }

    public Stream<Comment> getComments() {
        return comments.get().stream();
    }

    public Stream<Chat> getChats() {
        return chats.get().stream();
    }

    public Stream<Rating> getRatings() {
        return ratings.get().stream();
    }
}
