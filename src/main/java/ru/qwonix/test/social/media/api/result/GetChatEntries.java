package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.MessageDto;

import java.util.List;

public class GetChatEntries {
    public sealed interface Result {

        enum SenderNotFound implements GetChatEntries.Result {
            INSTANCE
        }

        enum RecipientNotFound implements GetChatEntries.Result {
            INSTANCE
        }

        record Success(List<MessageDto> messages) implements GetChatEntries.Result {
        }
    }
}
