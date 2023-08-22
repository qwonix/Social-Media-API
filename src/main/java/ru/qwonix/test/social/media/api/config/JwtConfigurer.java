package ru.qwonix.test.social.media.api.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.qwonix.test.social.media.api.security.TokenAuthenticationConverter;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;

@RequiredArgsConstructor
public class JwtConfigurer extends AbstractHttpConfigurer<JwtConfigurer, HttpSecurity> {
    private final AuthenticationService authenticationService;
    private RequestMatcher authenticationRequestMatcher = new AntPathRequestMatcher("/api/v1/auth", HttpMethod.POST.name());


    @Override
    public void init(HttpSecurity builder) {
        var csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(authenticationRequestMatcher);
        }
    }

    @Override
    public void configure(HttpSecurity builder) {
        final var authenticationManager =
                builder.getSharedObject(AuthenticationManager.class);

        var jwtAuthenticationFilter = getAuthenticationFilter(authenticationManager);

        var preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
        // simple UserDetailsService that retrieves the necessary data from the PreAuthenticatedAuthenticationToken
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(token ->
                new User(token.getName(), token.getCredentials().toString(), token.getAuthorities()));

        builder
                .authenticationProvider(preAuthenticatedAuthenticationProvider)
                .addFilterAfter(jwtAuthenticationFilter, CsrfFilter.class);
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        var jwtAuthenticationFilter = new AuthenticationFilter(
                authenticationManager,
                new TokenAuthenticationConverter(authenticationService)
        );
        // all requests except those related to authentication
        jwtAuthenticationFilter.setRequestMatcher(new NegatedRequestMatcher(authenticationRequestMatcher));
        jwtAuthenticationFilter.setSuccessHandler((request, response, authentication) -> CsrfFilter.skipRequest(request));
        jwtAuthenticationFilter.setFailureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        jwtAuthenticationFilter.setBeanName("JWT Authentication Filter");
        return jwtAuthenticationFilter;
    }

    public JwtConfigurer authenticationRequestMatcher(RequestMatcher requestMatcher) {
        this.authenticationRequestMatcher = requestMatcher;
        return this;
    }

}
