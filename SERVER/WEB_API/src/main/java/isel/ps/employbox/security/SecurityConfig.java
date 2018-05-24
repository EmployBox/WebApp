package isel.ps.employbox.security;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.model.entities.Account;
import javafx.util.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.RESOURCE_NOTFOUND_USER;

@EnableWebFluxSecurity
public class SecurityConfig {

    private final DataRepository<Account, Long> accountRepo;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public SecurityConfig(DataRepository<Account, Long> accountRepo, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.accountRepo = accountRepo;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
        // Disable default security.
        http.httpBasic().disable();
        http.formLogin().disable();
        http.csrf().disable();
        http.logout().disable();

        // Add custom security.
        http.authenticationManager(authenticationManager);
        http.securityContextRepository(securityContextRepository);

        // Disable authentication for `/auth/**` routes.
        http.authorizeExchange().pathMatchers("/accounts/users").permitAll();
        http.authorizeExchange().anyExchange().authenticated();

        return http.build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(){
        return username -> Mono.fromFuture(
                accountRepo.findWhere(new Pair<>("email", username))
                .thenApply(accounts -> accounts.get(0))
                .thenApply(account -> {
                    if (account != null)
                        return account;
                    throw new UsernameNotFoundException(RESOURCE_NOTFOUND_USER);
                })
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
