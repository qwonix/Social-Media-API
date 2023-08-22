package ru.qwonix.test.social.media.api.dto;

import java.util.List;

public record FeedDto(List<PostResponseDto> posts) {
}
