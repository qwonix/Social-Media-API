package ru.qwonix.test.social.media.api.result;

public record RegisterUserEntries() {

    public sealed interface Result {
        enum UsernameAlreadyExists implements Result {
            INSTANCE
        }

        enum EmailAlreadyExists implements Result {
            INSTANCE
        }

        enum Success implements Result {
            INSTANCE
        }
    }
}
