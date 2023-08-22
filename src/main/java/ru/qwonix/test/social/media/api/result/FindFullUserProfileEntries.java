package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.FullUserProfileResponse;

public class FindFullUserProfileEntries {
    public sealed interface Result {
        enum NotFound implements FindFullUserProfileEntries.Result {
            INSTANCE
        }

        record Success(FullUserProfileResponse userProfileResponse) implements FindFullUserProfileEntries.Result {
        }
    }
}
