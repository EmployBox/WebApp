package isel.ps.employbox.model.input;

public class InCompany {
    private long accountId;
    private String name;
    private String email;
    private String password;
    private float rating;
    private String specialization;
    private short yearFounded;
    private String logoUrl;
    private String webpageUrl;
    private String description;
    private long companyVersion;
    private long accountVersion;


    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public float getRating() { return rating; }

    public void setRating(float rating) { this.rating = rating; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }

    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public short getYearFounded() { return yearFounded; }

    public void setYearFounded(short yearFounded) { this.yearFounded = yearFounded; }

    public String getLogoUrl() { return logoUrl; }

    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getWebpageUrl() { return webpageUrl; }

    public void setWebpageUrl(String webpageUrl) { this.webpageUrl = webpageUrl; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public long getAccountId() { return accountId; }

    public void setAccountId(long accountId) { this.accountId = accountId; }

    public long getCompanyVersion() {
        return companyVersion;
    }

    public void setCompanyVersion(long companyVersion) {
        this.companyVersion = companyVersion;
    }

    public long getAccountVersion() {
        return accountVersion;
    }

    public void setAccountVersion(long accountVersion) {
        this.accountVersion = accountVersion;
    }
}
