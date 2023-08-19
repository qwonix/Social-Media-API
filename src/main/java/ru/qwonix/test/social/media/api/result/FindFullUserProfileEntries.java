package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.FullUserProfileResponseDto;

public class FindFullUserProfileEntries {
    public sealed interface Result {
        enum NotFound implements FindFullUserProfileEntries.Result {
            INSTANCE
        }

        record Success(FullUserProfileResponseDto userProfileResponseDto) implements FindFullUserProfileEntries.Result {
        }
    }
}
