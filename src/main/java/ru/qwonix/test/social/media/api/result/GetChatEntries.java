package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.MessageResponse;

import java.util.List;

public class GetChatEntries {
    public sealed interface Result {

        enum SenderNotFound implements GetChatEntries.Result {
            INSTANCE
        }

        enum RecipientNotFound implements GetChatEntries.Result {
            INSTANCE
        }

        record Success(List<MessageResponse> messages) implements GetChatEntries.Result {
        }
    }
}
