package ru.qwonix.test.social.media.api.result;

public class DeletePostEntries {
    public sealed interface Result {

        enum NotFound implements DeletePostEntries.Result {
            INSTANCE
        }

        enum Success implements DeletePostEntries.Result {
            INSTANCE
        }
    }
}
