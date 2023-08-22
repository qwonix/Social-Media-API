package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.SendMessageDto;
import ru.qwonix.test.social.media.api.result.GetChatEntries;
import ru.qwonix.test.social.media.api.result.SendMessageEntries;

public interface ChatFacade {
    SendMessageEntries.Result sendMessage(String senderUsername, String recipientUsername, SendMessageDto sendMessageDto);

    GetChatEntries.Result getChatPaginated(String senderUsername, String recipientUsername, Integer page, Integer size);
}
