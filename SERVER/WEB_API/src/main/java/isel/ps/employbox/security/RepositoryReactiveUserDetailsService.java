package isel.ps.employbox.security;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.model.entities.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;

@Component
public class RepositoryReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Account,Long> accountMapper = getMapper(Account.class, unitOfWork);

        return Mono.fromFuture(
                accountMapper
                        .findWhere( new Pair<>("email", username))
                        .thenCompose( res -> unitOfWork.commit().thenApply( aVoid -> res))
                        .thenApply(accounts -> {
                            if (!accounts.isEmpty())
                                return new CustomUserDetails(accounts.get(0));
                            return null;
                        })
        );
    }

    static class CustomUserDetails extends Account implements UserDetails {
        CustomUserDetails(Account account) {
            super(
                    account.getIdentityKey(),
                    account.getName(),
                    account.getEmail(),
                    account.getPassword(),
                    account.getAccountType(),
                    account.getRating(),
                    account.getVersion(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
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
