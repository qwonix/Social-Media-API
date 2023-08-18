package ru.qwonix.test.social.media.api.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.qwonix.test.social.media.api.entity.Token;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationConverter implements AuthenticationConverter {
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    private final AuthenticationService authenticationService;


    @Override
    public Authentication convert(HttpServletRequest request) {
        log.info("token authentication request");
        final var token = obtainToken(request);
        if (token != null) {
            Token accessToken;
            try {
                accessToken = authenticationService.getAccessToken(token);
            } catch (ExpiredJwtException e) {
                throw new CredentialsExpiredException("Token expired");
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
                throw new BadCredentialsException("Invalid token");
            } catch (IllegalArgumentException e) {
                throw new BadCredentialsException("Token string is null or empty or only whitespace");
            }
            if (accessToken != null) {
                return new PreAuthenticatedAuthenticationToken(accessToken.subject(), token, accessToken.permissions());
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
