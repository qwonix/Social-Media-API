package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.MessageResponse;

import java.util.List;

public class FindChatEntries {
    public sealed interface Result {

        enum SenderNotFound implements FindChatEntries.Result {
            INSTANCE
        }

        enum RecipientNotFound implements FindChatEntries.Result {
            INSTANCE
        }

        record Success(List<MessageResponse> messages) implements FindChatEntries.Result {
        }
    }
}
