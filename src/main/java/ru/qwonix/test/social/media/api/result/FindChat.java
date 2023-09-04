package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.MessageResponse;

import java.util.List;

public class FindChat {
    public sealed interface Result {

        enum SenderNotFound implements FindChat.Result {
            INSTANCE
        }

        enum RecipientNotFound implements FindChat.Result {
            INSTANCE
        }

        record Success(List<MessageResponse> messages) implements FindChat.Result {
        }
    }
}
