package ru.qwonix.test.social.media.api.dto;

import java.time.LocalDateTime;

public record MessageResponse(PublicUserProfileResponse sender, PublicUserProfileResponse recipient, String text,
                              LocalDateTime sendingTime) {
}
