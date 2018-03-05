package model;

public class Company extends Account {
    private final String name;
    private final String Specialization;
    private final short yearFounded;
    private final String logoUrl;
    private final String webPageUrl;
    private final String description;

    private Company(
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
            String description
    ){
        super(accountID,email,password,rating,version);
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
            String description
    ){
        Company company = new Company(defaultKey, email, password, rating, version, name, specialization, yearFounded, logoUrl, webPageUrl, description);
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
            String description
    ){
        Company company = new Company(accountID, email, password, rating, version, name, specialization, yearFounded, logoUrl, webPageUrl, description);
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
           String description
    ){
        company.markToBeDirty();
        Company newCompany = new Company(company.getIdentityKey(), email, password, rating, company.getNextVersion(), name, specialization, yearFounded, logoUrl, webPageUrl, description);
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
