package ru.qwonix.test.social.media.api.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record Token(String subject, Collection<? extends GrantedAuthority> authorities) {
}
