package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.qwonix.test.social.media.api.dto.MessageResponse;
import ru.qwonix.test.social.media.api.dto.SendMessageRequest;
import ru.qwonix.test.social.media.api.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "sendingTime", source = "createdAt")
    MessageResponse map(Message message);

    Message map(SendMessageRequest sendMessageRequest);
}
