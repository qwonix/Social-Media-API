package ru.qwonix.test.social.media.api.entity;


import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    READ, WRITE;

    @Override
    public String getAuthority() {
        return name();
    }
}