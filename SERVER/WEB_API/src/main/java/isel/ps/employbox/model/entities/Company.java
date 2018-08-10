package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.annotations.Version;

import java.util.List;

public class Company extends Account {
    private final String specialization;
    private final Integer yearFounded;
    private final String logoUrl;
    private final String webPageUrl;
    private final String description;
    @Version
    private final long version;

    public Company(){
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
            int yearFounded,
            String logoUrl,
            String webPageUrl,
            String description,
            List<Job> offeredJobs,
            List<Chat> chats,
            List<Comment> comments,
            List<Rating> ratings,
            List<Account> following,
            List<Account> followers)
    {
        super(accountID, name, email, password, "CMP", rating, version, offeredJobs,comments, chats, ratings, following, followers);
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
            int yearFounded,
            String logoUrl,
            String webPageUrl,
            String description,
            long version,
            long accountVersion) {
        super(accountID, name, email, password, "CMP", rating, accountVersion);
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

    public Integer getYearFounded() {
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
