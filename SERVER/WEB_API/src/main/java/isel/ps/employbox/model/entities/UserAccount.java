package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.ColumnName;
import com.github.jayield.rapper.Version;
import com.github.jayield.rapper.utils.UnitOfWork;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class UserAccount extends Account {
    private final String summary;
    private final String photoUrl;
    @Version
    private final long version;

    @ColumnName(foreignName = "accountId")
    private final Function<UnitOfWork, CompletableFuture<List<Curriculum>>> curricula;
    @ColumnName(foreignName = "accountId")
    private final Function<UnitOfWork, CompletableFuture<List<Application>>> applications;

    public UserAccount(){
        summary = null;
        photoUrl = null;
        curricula = null;
        applications = null;
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
            List<Job> offeredJobs,
            List<Curriculum> curricula,
            List<Application> applications,
            List<Chat> chats,
            List<Comment> comments,
            List<Rating> ratings,
            List<Account> following,
            List<Account> followers,
            long version
    ) {
        super(accountID, name, email, password, "USR", rating, accountVersion, offeredJobs, comments, chats, ratings,following, followers);
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.curricula = (__) -> CompletableFuture.completedFuture(curricula);
        this.applications = (__) -> CompletableFuture.completedFuture(applications);
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
        super(accountId, name, email, password, "USR", rating, accountVersion);
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.version = version;
        this.curricula = null;
        this.applications = null;
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


    @Override
    public long getVersion() {
        return version;
    }

    public long getAccountVersion() {
        return super.getVersion();
    }

    public Function<UnitOfWork, CompletableFuture<List<Curriculum>>> getCurricula() {
        return curricula;
    }

    public Function<UnitOfWork, CompletableFuture<List<Application>>> getApplications() {
        return applications;
    }
}
