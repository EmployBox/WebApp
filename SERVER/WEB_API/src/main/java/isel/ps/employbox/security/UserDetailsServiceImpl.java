package isel.ps.employbox.security;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.model.entities.Account;
import javafx.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static isel.ps.employbox.ErrorMessages.RESOURCE_NOTFOUND_USER;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DataRepository<Account, Long> accountRepo;

    public UserDetailsServiceImpl(DataRepository<Account, Long> accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return accountRepo
                .findWhere(new Pair<>("email", email))
                .join()
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(RESOURCE_NOTFOUND_USER));
    }
}
