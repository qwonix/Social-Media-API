package ru.qwonix.test.social.media.api.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Set;

@Getter
public enum Role {
    BANNED(Collections.emptySet()),
    USER(Set.of(Permission.READ, Permission.UPLOAD_IMAGE)),
    ADMIN(Set.of(Permission.READ, Permission.WRITE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<? extends GrantedAuthority> getAuthorities() {
        return getPermissions();
    }
}