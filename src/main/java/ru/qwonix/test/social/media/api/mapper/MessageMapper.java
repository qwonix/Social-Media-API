package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.qwonix.test.social.media.api.dto.MessageDto;
import ru.qwonix.test.social.media.api.dto.SendMessageDto;
import ru.qwonix.test.social.media.api.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "sendingTime", source = "createdAt")
    MessageDto map(Message message);

    Message map(SendMessageDto sendMessageDto);
}
