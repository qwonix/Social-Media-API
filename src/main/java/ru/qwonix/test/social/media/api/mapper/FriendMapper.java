package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import ru.qwonix.test.social.media.api.dto.RelationDto;
import ru.qwonix.test.social.media.api.entity.Relation;

@Mapper(componentModel = "spring")
public interface FriendMapper {
    RelationDto map(Relation relation);
}
