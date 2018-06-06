package isel.ps.employbox.security;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.model.entities.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

@Component
public class RepositoryReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final DataRepository<Account, Long> accountRepo;

    public RepositoryReactiveUserDetailsService(DataRepository<Account, Long> accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromFuture(
                accountRepo
                        .findWhere(new Pair<>("email", username))
                        .thenApply(accounts -> {
                            if (!accounts.isEmpty())
                                return new CustomUserDetails(accounts.get(0));
                            return null;
                        })
        );
    }

    static class CustomUserDetails extends Account implements UserDetails {
        CustomUserDetails(Account account) {
            super(account.getIdentityKey(), account.getEmail(), account.getPassword(), account.getRating(), account.getVersion(), account.getOfferedJobs(),
                    account.getComments(), account.getChats(), account.getRatings(), account.getFollowing(), account.getFollowers());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList();
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
    }
}
