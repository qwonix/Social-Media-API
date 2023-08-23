package ru.qwonix.test.social.media.api.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.qwonix.test.social.media.api.entity.Token;
import ru.qwonix.test.social.media.api.exception.TokenValidationException;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationConverter implements AuthenticationConverter {
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    private final AuthenticationService authenticationService;

    @Override
    public Authentication convert(HttpServletRequest request) {
        final var token = obtainToken(request);
        if (token != null) {
            Token accessToken;
            try {
                accessToken = authenticationService.parseAccessToken(token);
            } catch (TokenValidationException e) {
                throw new BadCredentialsException("Token is invalid", e);
            }
            if (accessToken != null) {
                return new PreAuthenticatedAuthenticationToken(accessToken.subject(), token, accessToken.authorities());
            }
        }
        return null;
    }

    /**
     * @param request so that request attributes can be retrieved
     * @return the token that will be presented in the Authentication request token to the AuthenticationManager
     */
    private String obtainToken(HttpServletRequest request) {
        final var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith(AUTHENTICATION_SCHEME)) {
            return authorization.substring(AUTHENTICATION_SCHEME.length() + 1);
        }
        return null;
    }
}
