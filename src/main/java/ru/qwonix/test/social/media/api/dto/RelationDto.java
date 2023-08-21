package ru.qwonix.test.social.media.api.dto;

import ru.qwonix.test.social.media.api.entity.RelationType;

public record RelationDto(PublicUserProfileResponseDto friend, RelationType relationType) {
}
