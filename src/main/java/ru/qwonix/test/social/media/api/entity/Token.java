package ru.qwonix.test.social.media.api.entity;

import java.util.List;

public record Token(String subject, List<Permission> permissions) {
}
