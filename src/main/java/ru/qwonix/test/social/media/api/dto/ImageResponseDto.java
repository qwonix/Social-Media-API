package ru.qwonix.test.social.media.api.dto;

import org.springframework.core.io.Resource;

public record ImageResponseDto(String name, Resource resource) {
}
