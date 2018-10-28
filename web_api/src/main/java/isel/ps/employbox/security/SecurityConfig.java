package isel.ps.employbox.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;
import java.util.Collections;

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
        http.authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/").permitAll()
                .pathMatchers(HttpMethod.GET,"/accounts/users").permitAll()
                .pathMatchers(HttpMethod.POST,"/accounts/users").permitAll()
                .pathMatchers(HttpMethod.GET,"/accounts/users/*").permitAll()
                .pathMatchers(HttpMethod.GET,"/accounts/users/*/applications").permitAll()
                .pathMatchers(HttpMethod.GET,"/accounts/users/*/jobs/*/applications/*").permitAll()
                .pathMatchers(HttpMethod.GET,"/accounts/users/*/jobs/*/applications").permitAll()
                .pathMatchers(HttpMethod.GET,"/accounts/users/*/curricula").permitAll()
                .pathMatchers(HttpMethod.GET,"/accounts/users/*/curricula/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/accounts/companies").permitAll()
                .pathMatchers(HttpMethod.GET, "/accounts/companies").permitAll()
                .pathMatchers(HttpMethod.GET, "/accounts/companies/*").permitAll()
                .pathMatchers(HttpMethod.GET, "/accounts/*/applications").permitAll()
                .pathMatchers(HttpMethod.GET, "/accounts/*/ratings").permitAll()
                .pathMatchers(HttpMethod.GET, "/accounts/*").permitAll()
                .pathMatchers(HttpMethod.GET, "/accounts/*/comments").permitAll()
                .pathMatchers(HttpMethod.GET, "/jobs/*/schedules").permitAll()
                .pathMatchers(HttpMethod.GET, "/jobs/*/applications").permitAll()
                .pathMatchers(HttpMethod.GET, "/jobs").permitAll()
                .pathMatchers(HttpMethod.GET, "/jobs/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyExchange().authenticated();

        //Activate security
        http.httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
