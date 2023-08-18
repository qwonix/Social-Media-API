package ru.qwonix.test.social.media.api.serivce;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.GrantedAuthority;
import ru.qwonix.test.social.media.api.entity.Token;

import java.util.Collection;

public interface AuthenticationService {
    String generateToken(String username, Collection<? extends GrantedAuthority> authorities);

    Token getAccessToken(String token) throws
            ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
}
