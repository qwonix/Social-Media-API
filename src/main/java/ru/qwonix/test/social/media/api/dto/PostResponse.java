package ru.qwonix.test.social.media.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostResponse(UUID id, String title, String text, LocalDateTime createdAt,
                           List<ImageResponse> images,
                           PublicUserProfileResponse owner) {
}
