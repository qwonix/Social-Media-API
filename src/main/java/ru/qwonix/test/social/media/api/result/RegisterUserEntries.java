package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.FullUserProfileResponseDto;

public record RegisterUserEntries() {

    public sealed interface Result {
        enum UsernameAlreadyExists implements Result {
            INSTANCE
        }

        enum EmailAlreadyExists implements Result {
            INSTANCE
        }

        record Success(FullUserProfileResponseDto userProfileResponseDto) implements Result {
        }
    }
}
