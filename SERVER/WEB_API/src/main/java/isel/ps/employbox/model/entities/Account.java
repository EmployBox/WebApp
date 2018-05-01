package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.ColumnName;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @ColumnName(foreignName = "accountId")
    protected final CompletableFuture<List<Job>> offeredJobs;

    @ColumnName(foreignName = "accountIdFrom")
    protected final CompletableFuture<List<Comment>> comments;

    @ColumnName(table = "Follows", foreignName = "accountIdFollowed", externalName = "accountIdFollower")
    protected final CompletableFuture<List<User>> following;

    @ColumnName( foreignName = "accountIdFirst")
    protected final CompletableFuture<List<Chat>> chats;

    @ColumnName(foreignName = "accountIdTo")
    protected final CompletableFuture<List<Rating>> ratings;


    public Account(){
        accountId = 0;
        email = null;
        password = null;
        rating = 0;
        version = 0;
        offeredJobs = null;
        comments = null;
        following = null;
        chats = null;
        ratings = null;
    }

    protected Account(
            long accountID,
            String email,
            String password,
            float rating,
            long version,
            CompletableFuture<List<Job>> offeredJobs,
            CompletableFuture<List<Comment>> comments,
            CompletableFuture<List<Chat>> chats,
            CompletableFuture<List<Rating>> ratings,
            CompletableFuture<List<User>> following
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
        this.offeredJobs = null;
        this.chats = null;
        this.ratings = null;
        this.comments = null;
        this.following = null;
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

    public CompletableFuture<List<Job>> getOfferedJobs() {
        return offeredJobs;
    }

    public CompletableFuture<List<User>> getFollowing() {
        return following;
    }

    public CompletableFuture<List<Comment>> getComments() {
        return comments;
    }

    public CompletableFuture<List<Chat>> getChats() {
        return chats;
    }

    public CompletableFuture<List<Rating>> getRatings() {
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
