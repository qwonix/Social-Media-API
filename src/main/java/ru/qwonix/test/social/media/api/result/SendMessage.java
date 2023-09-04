package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.MessageResponse;

public class SendMessage {
    public sealed interface Result {

        enum SenderNotFound implements SendMessage.Result {
            INSTANCE
        }

        enum RecipientNotFound implements SendMessage.Result {
            INSTANCE
        }

        enum NonFriends implements SendMessage.Result {
            INSTANCE
        }

        record Success(MessageResponse message) implements SendMessage.Result {
        }
    }
}
