package isel.ps.employbox.security;

import isel.ps.employbox.services.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AuthFilter extends WebSecurityConfigurerAdapter {
    private AccountService accountService;

    public AuthFilter(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/accounts/users").permitAll()
                .antMatchers(HttpMethod.GET,"/accounts/users/{\\d+}").permitAll()
                .antMatchers(HttpMethod.GET,"/accounts/users/{\\d+}/applications").permitAll()
                .antMatchers(HttpMethod.GET,"/accounts/users/{\\d+}/applications/{jid}").permitAll()
                .antMatchers(HttpMethod.POST, "/accounts/companies").permitAll()
                .antMatchers(HttpMethod.GET, "/accounts/companies").permitAll()
                .antMatchers(HttpMethod.GET, "/accounts/companies/{\\d+}").permitAll()
                .antMatchers(HttpMethod.GET, "/accounts/{\\d+}/applications").permitAll()
                .antMatchers(HttpMethod.GET, "/accounts/{\\d+}/ratings").permitAll()
                .antMatchers(HttpMethod.GET, "/accounts/{\\d+}/comments").permitAll()
                .antMatchers(HttpMethod.GET, "/jobs").permitAll()
                .antMatchers(HttpMethod.GET, "/jobs/{\\d+}").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
    }
}