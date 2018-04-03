package isel.ps.employbox.model.output;

public class OutCompany {
    private String accountId;
    private String email;
    private float rating;
    private String name;
    private String specialization;
    private short yearFounded;
    private String logoUrl;
    private String webpageUrl;
    private String description;

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

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

    public String getAccountId() { return accountId; }

    public void setAccountId(String accountId) { this.accountId = accountId; }
}
