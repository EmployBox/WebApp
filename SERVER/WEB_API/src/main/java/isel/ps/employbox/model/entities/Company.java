package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.Version;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Company extends Account {
    private final String name;
    private final String specialization;
    private final Short yearFounded;
    private final String logoUrl;
    private final String webPageUrl;
    private final String description;
    @Version
    private final long version;

    public Company(){
        name = null;
        specialization = null;
        yearFounded = null;
        logoUrl = null;
        webPageUrl = null;
        description = null;
        version = 0;
    }

    public Company(
            long accountID,
            String email,
            String password,
            float rating,
            long version,
            String name,
            String specialization,
            short yearFounded,
            String logoUrl,
            String webPageUrl,
            String description,
            CompletableFuture<List<Job>> offeredJobs,
            CompletableFuture<List<Chat>> chats,
            CompletableFuture<List<Comment>> comments,
            CompletableFuture<List<Rating>> ratings,
            CompletableFuture<List<Account>> following,
            CompletableFuture<List<Account>> followers)
    {
        super(accountID, email, password, rating, version, offeredJobs,comments, chats, ratings, following, followers);
        this.name = name;
        this.specialization = specialization;
        this.logoUrl = logoUrl;
        this.webPageUrl = webPageUrl;
        this.description = description;
        this.yearFounded = yearFounded;
        this.version = 0;
    }

    public Company(
            long accountID,
            String email,
            String password,
            float rating,
            String name,
            String specialization,
            short yearFounded,
            String logoUrl,
            String webPageUrl,
            String description,
            long version,
            long accountVersion) {
        super(accountID, email, password, rating, accountVersion);
        this.name = name;
        this.specialization = specialization;
        this.logoUrl = logoUrl;
        this.webPageUrl = webPageUrl;
        this.description = description;
        this.yearFounded = yearFounded;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public Short getYearFounded() {
        return yearFounded;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getWebPageUrl() {
        return webPageUrl;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public long getAccountVersion() {
        return super.getVersion();
    }
}
