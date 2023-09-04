package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.SendMessageRequest;
import ru.qwonix.test.social.media.api.result.FindChat;
import ru.qwonix.test.social.media.api.result.SendMessage;

public interface ChatFacade {
    SendMessage.Result sendMessage(String senderUsername, String recipientUsername, SendMessageRequest sendMessageRequest);

    FindChat.Result findChatPaginated(String senderUsername, String recipientUsername, Integer page, Integer size);
}
