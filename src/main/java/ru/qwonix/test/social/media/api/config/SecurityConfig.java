package ru.qwonix.test.social.media.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;
import ru.qwonix.test.social.media.api.serivce.impl.JwtAuthenticationService;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
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
                                .requestMatchers("/api/v1/image/*").permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/v3/api-docs",
                                        "/v3/api-docs/swagger-config",
                                        "/favicon.ico",
                                        "/swagger-ui/**").permitAll()
                                .anyRequest().authenticated()
                )
        ;

        return http.build();
    }

    @Bean
    public AuthenticationService authenticationService(JwtTokenProperties jwtTokenProperties) {
        return new JwtAuthenticationService(jwtTokenProperties.getSecret(), jwtTokenProperties.getTtl());
    }

    @Bean
    public JwtConfigurer jwtConfigurer(AuthenticationService authenticationService) {
        return new JwtConfigurer(authenticationService)
                .authenticationRequestMatcher(new AntPathRequestMatcher("/api/v1/auth", HttpMethod.POST.name()));
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
