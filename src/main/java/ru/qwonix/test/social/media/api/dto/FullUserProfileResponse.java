package ru.qwonix.test.social.media.api.dto;

import java.util.UUID;

public record FullUserProfileResponse(UUID id, String username, String email) {
}
