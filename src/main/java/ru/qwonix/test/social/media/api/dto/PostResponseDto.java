package ru.qwonix.test.social.media.api.dto;

public record PostResponseDto(Long id, String title, String text, /*LocalDateTime createdAt,*/
                              PublicUserProfileResponseDto owner) {
}
