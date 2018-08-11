package isel.ps.employbox.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securitygwebfilterchain(ServerHttpSecurity http) {
        // Disable default security.
        //http.httpBasic().disable();
        http.formLogin().disable();
        http.csrf().disable();
        http.logout().disable();

        // Add custom security.
        //http.authenticationManager(authenticationManager);
        //http.securityContextRepository(securityContextRepository);

        // Disable authentication for the selected routes.
        http.authorizeExchange().pathMatchers(HttpMethod.GET, "/").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/accounts/users").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.POST,"/accounts/users").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/accounts/users/*").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/accounts/users/*/applications").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/accounts/*/ratings/*/single").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/accounts/users/*/jobs/*/applications/*").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/accounts/users/*/jobs/*/applications").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/accounts/users/*/curricula").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/accounts/users/*/curricula/**").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.POST, "/accounts/companies").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET, "/accounts/companies").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET, "/accounts/companies/*").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET, "/accounts/*/applications").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET, "/accounts/*/ratings").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET, "/accounts/*/comments").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET, "/jobs").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET, "/jobs/**").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        http.authorizeExchange().anyExchange().authenticated();

        //Activate security
        http.httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
