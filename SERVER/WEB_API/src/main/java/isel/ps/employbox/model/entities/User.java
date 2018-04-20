package isel.ps.employbox.model.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class User extends Account implements UserDetails, Serializable {
    private final String name;
    private final String summary;
    private final String photoUrl;
    private final Supplier<List<Curriculum>> curricula;
    private final Supplier<List<Application>> applications;

    public User(){
        name = null;
        summary = null;
        photoUrl = null;
        curricula = Collections::emptyList;
        applications = Collections::emptyList;
    }

    public User(long accountID,
                String email,
                String password,
                float rating,
                long version,
                String name,
                String summary,
                String photoUrl,
                Supplier<List<Job>> offeredJobs,
                Supplier<List<Curriculum>> curricula,
                Supplier<List<Application>> applications,
                Supplier<List<Chat>> chats,
                Supplier<List<Comment>> comments,
                Supplier<List<Rating>> ratings,
                Supplier<List<User>> followers)
    {
        super(accountID, email, password, rating, version, offeredJobs, comments, chats, ratings, followers);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.curricula = curricula;
        this.applications = applications;
    }

    public User(String email, String password, float rating, String name, String summary, String photoUrl){
        super(email, password, rating);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
        this.curricula = Collections::emptyList;
        this.applications = Collections::emptyList;
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

    public Supplier<List<Application>> getApplications() {
        return applications;
    }

    public Supplier<List<Curriculum>> getCurricula() {
        return curricula;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
