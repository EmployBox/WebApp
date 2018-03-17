package isel.ps.employbox.api.model.input;

public class User {
    private final String email;
    private final String password;
    private final String name;
    private final String photo_url;
    private final String summary;

    public User(String email, String password, String name, String photo_url, String summary) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.photo_url = photo_url;
        this.summary = summary;
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
}