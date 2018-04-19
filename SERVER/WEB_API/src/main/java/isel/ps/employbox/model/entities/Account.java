package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Account implements DomainObject<Long> {
    protected long accountID;
    protected final String email;
    protected final String password;
    protected final float rating;
    protected final long version;
    protected final Supplier<List<Job>> offeredJobs;
    protected final Supplier<List<Comment>> comments;
    protected final Supplier<List<User>> following;
    protected final Supplier<List<Chat>> chats;
    protected final Supplier<List<Rating>> ratings;

    protected Account(long accountID, String email, String password, float rating, long version, Supplier<List<Job>> offeredJobs, Supplier<List<Comment>> comments,
            Supplier<List<Chat>> chats, Supplier<List<Rating>> ratings, Supplier<List<User>> following){
        this.accountID = accountID;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.version = version;
        this.offeredJobs = offeredJobs;
        this.chats = chats;
        this.ratings = ratings;
        this.comments = comments;
        this.following = following;
    }

    protected Account(String email, String password, float rating){
        this.accountID = -1;
        this.version = -1;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.offeredJobs = Collections::emptyList;
        this.chats = Collections::emptyList;
        this.ratings = Collections::emptyList;
        this.comments = Collections::emptyList;
        this.following = Collections::emptyList;
    }



    public long getAccountID() {
        return accountID;
    }

    @Override
    public Long getIdentityKey() {
        return null;
    }

    public long getVersion() {
        return version;
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

    public Supplier<List<Job>> getOfferedJobs() {
        return offeredJobs;
    }

    public Supplier<List<User>> getFollowing() {
        return following;
    }

    public Supplier<List<Comment>> getComments() {
        return comments;
    }

    public Supplier<List<Chat>> getChats() {
        return chats;
    }

    public Supplier<List<Rating>> getRatings() {
        return ratings;
    }
}
