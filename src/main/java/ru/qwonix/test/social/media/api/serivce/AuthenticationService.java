package ru.qwonix.test.social.media.api.serivce;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthenticationService {
    String generateToken(String username, Collection<? extends GrantedAuthority> authorities);
}
