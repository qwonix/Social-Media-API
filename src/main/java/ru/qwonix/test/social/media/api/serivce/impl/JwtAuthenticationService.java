package ru.qwonix.test.social.media.api.serivce.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import ru.qwonix.test.social.media.api.entity.Permission;
import ru.qwonix.test.social.media.api.entity.Token;
import ru.qwonix.test.social.media.api.exception.TokenValidationException;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class JwtAuthenticationService implements AuthenticationService {

    private static final String AUTHORITIES_CLAIM_NAME = "authorities";
    private final Key accessJwtKey;

    @Setter
    private Duration accessTokenTtl;

    /**
     * @param accessJwtKey   secret key used for signing JWT tokens
     * @param accessTokenTtl time-to-live duration for access tokens
     */
    public JwtAuthenticationService(SecretKey accessJwtKey, Duration accessTokenTtl) {
        this.accessJwtKey = accessJwtKey;
        this.accessTokenTtl = accessTokenTtl;
    }

    @Override
    public String serializeToken(Token token) {
        return generateAccessToken(token.subject(), token.authorities());
    }

    /**
     * Generates an JWT access token using the provided subject and authorities
     *
     * @param subject     subject of the token (typically the username)
     * @param authorities collection of authorities associated with the subject
     * @return generated JWT access token
     */
    private String generateAccessToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        final var accessExpirationInstant =
                LocalDateTime.now().plus(accessTokenTtl).atZone(ZoneId.systemDefault()).toInstant();
        var stringAuthorities = authorities.stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(subject)
                .claim(AUTHORITIES_CLAIM_NAME, stringAuthorities)
                .setExpiration(Date.from(accessExpirationInstant))
                .signWith(accessJwtKey)
                .compact();
    }

    @Override
    public Token parseAccessToken(String token) throws TokenValidationException {
        return parseToken(token, accessJwtKey);
    }

    /**
     * Parses and validates a JWT token, returning a Token object containing token authentication information
     *
     * @param token  JWT token to parse and validate
     * @param secret secret key used to validate the token's signature
     * @return Token object containing token authentication information
     * @throws TokenValidationException if the token is invalid
     */
    private Token parseToken(String token, Key secret) throws TokenValidationException {
        var claims = parseAndValidateJwt(token, secret);
        var authorities = extractAuthoritiesAndCreateToken(claims);

        return new Token(claims.getSubject(), authorities);
    }

    /**
     * Extracts authorities from the JWT claims and creates a collection of {@link Permission}
     *
     * @param claims The JWT claims containing authority information
     * @return collection of permissions extracted from the claims
     * @throws TokenValidationException If there's an issue with the authority claims
     */
    private Collection<Permission> extractAuthoritiesAndCreateToken(Claims claims) throws TokenValidationException {
        try {
            var stringAuthorities = claims.get(AUTHORITIES_CLAIM_NAME, List.class);
            var authorities = new HashSet<Permission>(stringAuthorities.size());
            for (Object authority : stringAuthorities) {
                var string = authority.toString();
                authorities.add(Permission.valueOf(string));
            }
            return authorities;
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new TokenValidationException("Invalid or missing authorities claim", e);
        }
    }

    /**
     * Parses and validates a JSON Web Token (JWT) using the provided secret key and returns the claims contained in the token
     *
     * @param token  JWT token string to be parsed and validated
     * @param secret secret key used for JWT validation
     * @return claims extracted from the validated JWT
     * @throws TokenValidationException if the token is invalid, expired, or the token string is null/empty/whitespace
     */
    private static Claims parseAndValidateJwt(String token, Key secret) throws TokenValidationException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new TokenValidationException("Invalid token", e);
        } catch (ExpiredJwtException e) {
            throw new TokenValidationException("Token expired", e);
        } catch (IllegalArgumentException e) {
            throw new TokenValidationException("Token string is null or empty or only whitespace", e);
        }
    }
}
