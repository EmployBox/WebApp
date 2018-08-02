package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static isel.ps.employbox.model.entities.Role.DEFAULT;

public class Account implements DomainObject<Long> {
    private static final Role role = DEFAULT;

    @Id(isIdentity =  true)
    protected long accountId;
    protected final String name;
    protected final String email;
    protected final String password;
    protected final String accountType;
    protected final double rating;
    @Version
    private final long version;

    @ColumnName(foreignName = "accountId")
    protected final Supplier<CompletableFuture<List<Job>>> offeredJobs;

    @ColumnName(foreignName = "accountIdFrom")
    protected final Supplier<CompletableFuture<List<Comment>>> comments;

    @ColumnName(table = "Follows", foreignName = "accountIdFollowed", externalName = "accountIdFollower")
    protected final Supplier< CompletableFuture<List<Account>>> following;

    @ColumnName(table = "Follows", foreignName = "accountIdFollower", externalName = "accountIdFollowed")
    protected final Supplier<CompletableFuture<List<Account>>> followers;

    @ColumnName( foreignName = "accountIdFirst")
    protected final Supplier<CompletableFuture<List<Chat>>> chats;

    @ColumnName(foreignName = "accountIdTo")
    protected final Supplier<CompletableFuture<List<Rating>>> ratings;


    public Account(){
        accountId = 0;
        name = null;
        email = null;
        password = null;
        rating = 0;
        accountType = null;
        version = 0;
        offeredJobs = null;
        comments = null;
        following = null;
        followers = null;
        chats = null;
        ratings = null;
    }

    protected Account(
            long accountID,
            String name, String email,
            String password,
            String accountType, double rating,
            long version,
            List<Job> offeredJobs,
            List<Comment> comments,
            List<Chat> chats,
            List<Rating> ratings,
            List<Account> following,
            List<Account> follower) {
        this.accountId = accountID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.rating = rating;
        this.version = version;
        this.offeredJobs = () -> CompletableFuture.completedFuture(offeredJobs);
        this.chats = () -> CompletableFuture.completedFuture(chats);
        this.ratings = () -> CompletableFuture.completedFuture(ratings);
        this.comments = () -> CompletableFuture.completedFuture(comments);
        this.following = () -> CompletableFuture.completedFuture(following);
        this.followers = () -> CompletableFuture.completedFuture(follower);
    }

    protected Account(long accountId, String name, String email, String password, String accountType, double rating, long version){
        this.accountId = accountId;
        this.name = name;
        this.accountType = accountType;
        this.version = version;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.offeredJobs =  null;
        this.chats =        null;
        this.ratings =      null;
        this.comments =     null;
        this.following =    null;
        this.followers =    null;
    }

    @Override
    public Long getIdentityKey() {
        return accountId;
    }

    public String getName() {
        return name;
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

    public String getAccountType() {
        return accountType;
    }

    public Supplier<CompletableFuture<List<Job>>> getOfferedJobs() {
        return offeredJobs;
    }

    public Supplier<CompletableFuture<List<Comment>>> getComments() {
        return comments;
    }

    public Supplier< CompletableFuture<List<Account>>> getFollowing() {
        return following;
    }

    public Supplier<CompletableFuture<List<Account>>> getFollowers() {
        return followers;
    }

    public Supplier<CompletableFuture<List<Chat>>> getChats() {
        return chats;
    }

    public Supplier<CompletableFuture<List<Rating>>> getRatings() {
        return ratings;
    }

    @Override
    public long getVersion() {
        return version;
    }

}
