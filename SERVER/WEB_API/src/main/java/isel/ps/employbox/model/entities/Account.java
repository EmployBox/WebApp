package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.ColumnName;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;
import com.github.jayield.rapper.Version;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.model.entities.Role.DEFAULT;

public class Account implements DomainObject<Long> {

    @Id (isIdentity =  true)
    protected long accountId;
    protected final String email;
    protected final String password;
    protected final double rating;
    private static final Role role = DEFAULT;
    @Version
    private final long version;

    @ColumnName(foreignName = "accountId")
    protected final CompletableFuture<List<Job>> offeredJobs;

    @ColumnName(foreignName = "accountIdFrom")
    protected final CompletableFuture<List<Comment>> comments;

    @ColumnName(table = "Follows", foreignName = "accountIdFollowed", externalName = "accountIdFollower")
    protected final CompletableFuture<List<Account>> following;

    @ColumnName(table = "Follows", foreignName = "accountIdFollower", externalName = "accountIdFollowed")
    protected final CompletableFuture<List<Account>> followers;

    @ColumnName( foreignName = "accountIdFirst")
    protected final CompletableFuture<List<Chat>> chats;

    @ColumnName(foreignName = "accountIdTo")
    protected final CompletableFuture<List<Rating>> ratings;


    public Account(){
        accountId = 0;
        email = null;
        password = null;
        rating = 0;
        version = 0;
        offeredJobs = CompletableFuture.completedFuture(Collections.emptyList());
        comments = CompletableFuture.completedFuture(Collections.emptyList());
        following = CompletableFuture.completedFuture(Collections.emptyList());
        followers = CompletableFuture.completedFuture(Collections.emptyList());
        chats = CompletableFuture.completedFuture(Collections.emptyList());
        ratings = CompletableFuture.completedFuture(Collections.emptyList());
    }

    protected Account(
            long accountID,
            String email,
            String password,
            double rating,
            long version,
            CompletableFuture<List<Job>> offeredJobs,
            CompletableFuture<List<Comment>> comments,
            CompletableFuture<List<Chat>> chats,
            CompletableFuture<List<Rating>> ratings,
            CompletableFuture<List<Account>> following,
            CompletableFuture<List<Account>> follower) {
        this.accountId = accountID;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.version = version;
        this.offeredJobs = offeredJobs;
        this.chats = chats;
        this.ratings = ratings;
        this.comments = comments;
        this.following = following;
        this.followers = follower;
    }

    protected Account(long accountId, String email, String password, double rating, long version){
        this.accountId = accountId;
        this.version = version;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.offeredJobs = CompletableFuture.completedFuture(Collections.emptyList());
        this.chats = CompletableFuture.completedFuture(Collections.emptyList());
        this.ratings = CompletableFuture.completedFuture(Collections.emptyList());
        this.comments = CompletableFuture.completedFuture(Collections.emptyList());
        this.following = CompletableFuture.completedFuture(Collections.emptyList());
        this.followers = CompletableFuture.completedFuture(Collections.emptyList());
    }

    @Override
    public Long getIdentityKey() {
        return accountId;
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

    public CompletableFuture<List<Job>> getOfferedJobs() {
        return offeredJobs;
    }

    public CompletableFuture<List<Account>> getFollowing() {
        return following;
    }

    public CompletableFuture<List<Account>> getFollowers() {
        return followers;
    }

    public CompletableFuture<List<Comment>> getComments() {
        return comments;
    }

    public CompletableFuture<List<Chat>> getChats() {
        return chats;
    }

    public CompletableFuture<List<Rating>> getRatings() {
        return ratings;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public Role getRole() {
        return role;
    }
}
