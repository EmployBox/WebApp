package isel.ps.employbox.model.input;

public class InUser {
    private long id;
    private String email;
    private String password;
    private String name;
    private String photo_url;
    private String summary;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoto_url() {
        return this.photo_url;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}