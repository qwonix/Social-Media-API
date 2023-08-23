package ru.qwonix.test.social.media.api.serivce.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Nonnull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import ru.qwonix.test.social.media.api.entity.Permission;
import ru.qwonix.test.social.media.api.entity.Token;
import ru.qwonix.test.social.media.api.exception.TokenValidationException;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class JwtAuthenticationService implements AuthenticationService {

    private static final String AUTHORITIES_CLAIM = "authorities";
    private final Key accessJwtKey;

    @Setter
    private Duration accessTokenTtl;

    /**
     * @param accessJwtSecret secret key used for signing JWT tokens
     * @param accessTokenTtl  time-to-live duration for access tokens
     */
    public JwtAuthenticationService(@Nonnull String accessJwtSecret, Duration accessTokenTtl) {
        this.accessJwtKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessJwtSecret));
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
        final Instant accessExpirationInstant =
                LocalDateTime.now().plus(accessTokenTtl).atZone(ZoneId.systemDefault()).toInstant();
        var stringAuthorities = authorities.stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(subject)
                .claim(AUTHORITIES_CLAIM, stringAuthorities)
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
        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return new Token(body.getSubject(),
                    body.get(AUTHORITIES_CLAIM, List.class).stream()
                            .map(it -> Permission.valueOf(it.toString()))
                            .toList());
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new TokenValidationException("Invalid token");
        } catch (ExpiredJwtException e) {
            throw new TokenValidationException("Token expired");
        } catch (IllegalArgumentException e) {
            throw new TokenValidationException("Token string is null or empty or only whitespace");
        }
    }
}
