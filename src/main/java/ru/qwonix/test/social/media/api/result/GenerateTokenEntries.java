package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.AuthenticationResponse;

public record GenerateTokenEntries() {

    public sealed interface Result {

        record Fail(String message) implements Result {
        }

        record Success(AuthenticationResponse authenticationResponse) implements Result {
        }
    }
}
