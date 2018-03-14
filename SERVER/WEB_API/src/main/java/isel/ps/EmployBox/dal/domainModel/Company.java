package isel.ps.EmployBox.dal.domainModel;

import isel.ps.EmployBox.dal.util.Streamable;

public class Company extends Account {
    private final String name;
    private final String Specialization;
    private final short yearFounded;
    private final String logoUrl;
    private final String webPageUrl;
    private final String description;

    public Company(
            long accountID,
            String email,
            String password,
            double rating,
            long version,
            String name,
            String Specialization,
            short yearFounded,
            String logoUrl,
            String webPageUrl,
            String description,
            Streamable<Job> offeredJobs,
            Streamable<Chat> chats,
            Streamable<Comment> comments,
            Streamable<Rating> ratings,
            Streamable<User> following
    ){
        super(accountID, email, password, rating, version, offeredJobs,comments, chats, ratings, following);
        this.name = name;
        this.Specialization = Specialization;
        this.logoUrl = logoUrl;
        this.webPageUrl = webPageUrl;
        this.description = description;
        this.yearFounded = yearFounded;
    }


    public static Company create(
            String email,
            String password,
            double rating,
            long version,
            String name,
            String specialization,
            short  yearFounded,
            String logoUrl,
            String webPageUrl,
            String description,
            Streamable<Job> offeredJobs,
            Streamable<Chat>chats,
            Streamable<Comment> comments,
            Streamable<Rating> ratings,
            Streamable<User> following
    ){
        Company company = new Company(defaultKey,
                email,
                password,
                rating,
                version,
                name,
                specialization,
                yearFounded,
                logoUrl,
                webPageUrl,
                description,
                offeredJobs,
                chats,
                comments,
                ratings,
                following
        );
        company.markNew();
        return company;
    }

    public static Company load(
            long accountID,
            String email,
            String password,
            double rating,
            long version,
            String name,
            String specialization,
            short  yearFounded,
            String logoUrl,
            String webPageUrl,
            String description,
            Streamable<Job> offeredJobs,
            Streamable<Chat>chats,
            Streamable<Comment> comments,
            Streamable<Rating> ratings,
            Streamable<User> following
    ){
        Company company = new Company(
                accountID,
                email,
                password,
                rating,
                version,
                name,
                specialization,
                yearFounded,
                logoUrl,
                webPageUrl,
                description,
                offeredJobs,
                chats,
                comments,
                ratings,
                following
        );
        company.markClean();
        return company;
    }

    public static Company update(
           Company company,
           String email,
           String password,
           double rating,
           String name,
           String specialization,
           short  yearFounded,
           String logoUrl,
           String webPageUrl,
           String description,
           Streamable<Job> offeredJobs,
           Streamable<Chat>chats,
           Streamable<Comment> comments,
           Streamable<Rating> ratings,
           Streamable<User> following
    ){
        company.markToBeDirty();
        Company newCompany = new Company(
                company.getIdentityKey(),
                email,
                password,
                rating,
                company.getNextVersion(),
                name, specialization,
                yearFounded,
                logoUrl,
                webPageUrl,
                description,
                offeredJobs,
                chats,
                comments,
                ratings,
                following
        );
        newCompany.markDirty();
        return newCompany;
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
}
