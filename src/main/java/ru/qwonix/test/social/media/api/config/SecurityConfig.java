package ru.qwonix.test.social.media.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;
import ru.qwonix.test.social.media.api.serivce.impl.AuthenticationServiceImpl;

import java.time.Duration;

@Configuration
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtConfigurer jwtConfigurer) throws Exception {
        http.apply(jwtConfigurer);

        http
                .httpBasic(Customizer.withDefaults())
                // fixme: not good
                .csrf(AbstractHttpConfigurer::disable)
                // REST don't create session
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // disable page caching
                .headers(headers -> headers.cacheControl(cacheControlConfig -> {
                }))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/api/v1/auth/register").permitAll()
                                .anyRequest().authenticated()
                )
        ;

        return http.build();
    }

    @Bean
    public AuthenticationService authenticationService(@Value("${jwt.secret.access}") String accessJwtSecret,
                                                       @Value("${jwt.ttl.access}") Duration accessTtl) {
        var authenticationService = new AuthenticationServiceImpl(accessJwtSecret);
        authenticationService.setAccessTokenTtl(accessTtl);
        return authenticationService;
    }

    @Bean
    public JwtConfigurer jwtConfigurer(AuthenticationService authenticationService) {
        return new JwtConfigurer(authenticationService)
                .authenticationRequestMatcher(new AntPathRequestMatcher("/api/v1/auth", HttpMethod.POST.name()));
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
