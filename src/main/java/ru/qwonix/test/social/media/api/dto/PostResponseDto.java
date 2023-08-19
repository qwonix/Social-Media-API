package ru.qwonix.test.social.media.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponseDto(UUID id, String title, String text, LocalDateTime createdAt,
                              PublicUserProfileResponseDto owner) {
}
