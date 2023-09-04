package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.FullUserProfileResponse;

public record RegisterUser() {

    public sealed interface Result {
        enum UsernameAlreadyExists implements Result {
            INSTANCE
        }

        enum EmailAlreadyExists implements Result {
            INSTANCE
        }

        record Success(FullUserProfileResponse userProfileResponse) implements Result {
        }
    }
}
