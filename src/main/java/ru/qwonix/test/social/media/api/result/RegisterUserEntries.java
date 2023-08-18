package ru.qwonix.test.social.media.api.result;

public record RegisterUserEntries() {

    public sealed interface Result {
        enum UserAlreadyExists implements Result {
            INSTANCE
        }

        enum Success implements Result {
            INSTANCE
        }
    }
}
