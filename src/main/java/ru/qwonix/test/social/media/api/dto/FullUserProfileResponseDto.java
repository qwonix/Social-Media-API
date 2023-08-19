package ru.qwonix.test.social.media.api.dto;

import java.util.UUID;

public record FullUserProfileResponseDto(UUID id, String username, String email) {
}
