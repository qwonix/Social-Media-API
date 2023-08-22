package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.MessageDto;

public class SendMessageEntries {
    public sealed interface Result {

        enum SenderNotFound implements SendMessageEntries.Result {
            INSTANCE
        }

        enum RecipientNotFound implements SendMessageEntries.Result {
            INSTANCE
        }

        enum NonFriends implements SendMessageEntries.Result {
            INSTANCE
        }

        record Success(MessageDto message) implements SendMessageEntries.Result {
        }
    }
}
