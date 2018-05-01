package isel.ps.employbox.model.entities;

import org.github.isel.rapper.ColumnName;
import org.github.isel.rapper.DomainObject;
import org.github.isel.rapper.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static isel.ps.employbox.model.entities.Role.DEFAULT;

public class Account implements DomainObject<Long>,UserDetails, Serializable {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Id (isIdentity =  true)
    protected long accountId;

    protected final String email;
    protected final String password;
    protected final double rating;
    private final Role role = DEFAULT;

    private final long version;

    @ColumnName(name = "accountId")
    protected final Supplier<List<Job>> offeredJobs;
    @ColumnName(name = "accountId")
    protected final Supplier<List<Comment>> comments;

    @ColumnName(table = "Follows", foreignName = "accountIdFollower", name = "accountIdFollowed")
    protected final Supplier<List<User>> following;

    @ColumnName(name = "accountId")
    protected final Supplier<List<Chat>> chats;

    @ColumnName(name = "accountIdTo")
    protected final Supplier<List<Rating>> ratings;


    public Account(){
        accountId = 0;
        email = null;
        password = null;
        rating = 0;
        version = 0;
        offeredJobs = Collections::emptyList;
        comments = Collections::emptyList;
        following = Collections::emptyList;
        chats = Collections::emptyList;
        ratings = Collections::emptyList;
    }

    protected Account(
            long accountID,
            String email,
            String password,
            float rating,
            long version,
            Supplier<List<Job>> offeredJobs,
            Supplier<List<Comment>> comments,
            Supplier<List<Chat>> chats,
            Supplier<List<Rating>> ratings,
            Supplier<List<User>> following
    ) {
        this.accountId = accountID;
        this.email = email;
        this.password = passwordEncoder.encode(password);
        this.rating = rating;
        this.version = version;
        this.offeredJobs = offeredJobs;
        this.chats = chats;
        this.ratings = ratings;
        this.comments = comments;
        this.following = following;
    }

    protected Account(long accountId, String email, String password, float rating, long version){
        this.accountId = accountId;
        this.version = version;
        this.email = email;
        this.password =  passwordEncoder.encode(password);
        this.rating = rating;
        this.offeredJobs = Collections::emptyList;
        this.chats = Collections::emptyList;
        this.ratings = Collections::emptyList;
        this.comments = Collections::emptyList;
        this.following = Collections::emptyList;
    }

    @Override
    public Long getIdentityKey() {
        return accountId;
    }


    public String getEmail() {
        return email;
    }


    public double getRating() {
        return rating;
    }

    public Supplier<List<Job>> getOfferedJobs() {
        return offeredJobs;
    }

    public Supplier<List<User>> getFollowing() {
        return following;
    }

    public Supplier<List<Comment>> getComments() {
        return comments;
    }

    public Supplier<List<Chat>> getChats() {
        return chats;
    }

    public Supplier<List<Rating>> getRatings() {
        return ratings;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
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

    @Override
    public long getVersion() {
        return version;
    }

    public Role getRole() {
        return role;
    }
}
