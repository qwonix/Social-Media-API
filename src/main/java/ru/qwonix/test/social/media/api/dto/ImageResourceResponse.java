package ru.qwonix.test.social.media.api.dto;

import org.springframework.core.io.Resource;

public record ImageResourceResponse(String name, Resource resource) {
}
