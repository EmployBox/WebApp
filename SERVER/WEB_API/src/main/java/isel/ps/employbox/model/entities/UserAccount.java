package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.ColumnName;
import com.github.jayield.rapper.Version;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserAccount extends Account {
    private final String name;
    private final String summary;
    private final String photoUrl;
    @Version
    private final long version;

    @ColumnName(foreignName = "accountId")
    private final CompletableFuture<List<Curriculum>> curricula;
    @ColumnName(foreignName = "accountId")
    private final CompletableFuture<List<Application>> applications;

    public UserAccount(){
        name = null;
        summary = null;
        photoUrl = null;
        curricula = CompletableFuture.completedFuture(Collections.emptyList());
        applications = CompletableFuture.completedFuture(Collections.emptyList());
        version = 0;
    }

    public UserAccount(
            long accountID,
            String email,
            String password,
            float rating,
            long accountVersion,
            String name,
            String summary,
            String photoUrl,
            CompletableFuture<List<Job>> offeredJobs,
            CompletableFuture<List<Curriculum>> curricula,
            CompletableFuture<List<Application>> applications,
            CompletableFuture<List<Chat>> chats,
            CompletableFuture<List<Comment>> comments,
            CompletableFuture<List<Rating>> ratings,
            CompletableFuture<List<Account>> following,
            CompletableFuture<List<Account>> followers,
            long version
    ) {
        super(accountID, email, password, rating, accountVersion, offeredJobs, comments, chats, ratings,following, followers);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.curricula = curricula;
        this.applications = applications;
        this.version = version;
    }

    public UserAccount(
            long accountId,
            String email,
            String password,
            float rating,
            String name,
            String summary,
            String photoUrl,
            long accountVersion,
            long version
    ){
        super(accountId, email, password, rating, accountVersion);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.version = version;
        this.curricula = CompletableFuture.completedFuture(Collections.emptyList());
        this.applications = CompletableFuture.completedFuture(Collections.emptyList());
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

    public CompletableFuture<List<Application>> getApplications() {
        return applications;
    }

    public CompletableFuture<List<Curriculum>> getCurricula() {
        return curricula;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public long getAccountVersion() {
        return super.getVersion();
    }
}
