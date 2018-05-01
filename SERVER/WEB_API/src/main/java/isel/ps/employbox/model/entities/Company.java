package isel.ps.employbox.model.entities;

import java.util.List;
import java.util.function.Supplier;

public class Company extends Account {
    private final String name;
    private final String Specialization;
    private final short yearFounded;
    private final String logoUrl;
    private final String webPageUrl;
    private final String description;
    private final long version;

    public Company(){
        name = null;
        Specialization = null;
        yearFounded = 0;
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
            String Specialization,
            short yearFounded,
            String logoUrl,
            String webPageUrl,
            String description,
            Supplier<List<Job>> offeredJobs,
            Supplier<List<Chat>> chats,
            Supplier<List<Comment>> comments,
            Supplier<List<Rating>> ratings,
            Supplier<List<User>> following)
    {
        super(accountID, email, password, rating, version, offeredJobs,comments, chats, ratings, following);
        this.name = name;
        this.Specialization = Specialization;
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
            String Specialization,
            short yearFounded, String logoUrl, String webPageUrl, String description, long version, long accountVersion) {
        super(accountID, email, password, rating, accountVersion);
        this.name = name;
        this.Specialization = Specialization;
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
        return Specialization;
    }

    public short getYearFounded() {
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
}
