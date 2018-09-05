package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.entities.jobs.Job;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Account implements DomainObject<Long> {

    @Id(isIdentity =  true)
    protected long accountId;
    protected final String name;
    protected final String email;
    protected final String password;
    protected final String accountType;
    public double rating;
    @Version
    private final long version;

    @ColumnName(foreignName = "accountId")
    protected final Function<UnitOfWork,CompletableFuture<List<Job>>> offeredJobs;

    @ColumnName(foreignName = "accountIdFrom")
    protected final Function<UnitOfWork, CompletableFuture<List<Comment>>> comments;

    @ColumnName(table = "Follows", foreignName = "accountIdFollowed", externalName = "accountIdFollower")
    protected final Function<UnitOfWork, CompletableFuture<List<Account>>> following;

    @ColumnName(table = "Follows", foreignName = "accountIdFollower", externalName = "accountIdFollowed")
    protected final Function<UnitOfWork, CompletableFuture<List<Account>>> followers;

    @ColumnName( foreignName = "accountIdFirst")
    protected final Function<UnitOfWork, CompletableFuture<List<Chat>>> chats;

    @ColumnName(foreignName = "accountIdTo")
    protected final Function<UnitOfWork, CompletableFuture<List<Rating>>> ratings;


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
        this.offeredJobs = (__) -> CompletableFuture.completedFuture(offeredJobs);
        this.chats = (__) -> CompletableFuture.completedFuture(chats);
        this.ratings = (__) -> CompletableFuture.completedFuture(ratings);
        this.comments = (__) -> CompletableFuture.completedFuture(comments);
        this.following = (__) -> CompletableFuture.completedFuture(following);
        this.followers = (__) -> CompletableFuture.completedFuture(follower);
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

    public Function<UnitOfWork, CompletableFuture<List<Job>>> getOfferedJobs() {
        return offeredJobs;
    }

    public Function<UnitOfWork, CompletableFuture<List<Comment>>> getComments() {
        return comments;
    }

    public Function<UnitOfWork, CompletableFuture<List<Account>>> getFollowing() {
        return following;
    }

    public Function<UnitOfWork, CompletableFuture<List<Account>>> getFollowers() {
        return followers;
    }

    public Function<UnitOfWork, CompletableFuture<List<Chat>>> getChats() {
        return chats;
    }

    public Function<UnitOfWork, CompletableFuture<List<Rating>>> getRatings() {
        return ratings;
    }

    @Override
    public long getVersion() {
        return version;
    }

}
