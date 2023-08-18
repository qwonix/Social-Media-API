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
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class AuthenticationServiceImpl implements AuthenticationService {

    public static final String AUTHORITIES_CLAIM = "authorities";
    private final Key accessJwtKey;

    @Setter
    private Duration accessTokenTtl = Duration.ofDays(1);

    public AuthenticationServiceImpl(@Nonnull String accessJwtSecret) {
        this.accessJwtKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessJwtSecret));
    }

    @Override
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return generateAccessToken(username, authorities);
    }

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
    public Token getAccessToken(String token) throws
            ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, io.jsonwebtoken.security.SignatureException, IllegalArgumentException {
        return getToken(token, accessJwtKey);
    }

    private Token getToken(String token, Key secret) throws
            ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new Token(body.getSubject(),
                body.get(AUTHORITIES_CLAIM, List.class).stream()
                        .map(it -> Permission.valueOf(it.toString()))
                        .toList());
    }
}
