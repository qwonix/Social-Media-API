package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.SendMessageRequest;
import ru.qwonix.test.social.media.api.result.GetChatEntries;
import ru.qwonix.test.social.media.api.result.SendMessageEntries;

public interface ChatFacade {
    SendMessageEntries.Result sendMessage(String senderUsername, String recipientUsername, SendMessageRequest sendMessageRequest);

    GetChatEntries.Result findChatPaginated(String senderUsername, String recipientUsername, Integer page, Integer size);
}
