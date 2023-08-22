package ru.qwonix.test.social.media.api.dto;

import java.time.LocalDateTime;

public record MessageDto(PublicUserProfileResponseDto sender, PublicUserProfileResponseDto recipient, String text, LocalDateTime sendingTime) {
}
