package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.qwonix.test.social.media.api.dto.MessageResponse;
import ru.qwonix.test.social.media.api.dto.SendMessageRequest;
import ru.qwonix.test.social.media.api.entity.Message;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    @Mapping(target = "sendingTime", source = "createdAt")
    MessageResponse map(Message message);

    Message map(SendMessageRequest sendMessageRequest);
}
