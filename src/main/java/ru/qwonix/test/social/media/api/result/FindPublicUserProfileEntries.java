package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PublicUserProfileResponse;

public class FindPublicUserProfileEntries {
    public sealed interface Result {
        enum NotFound implements FindPublicUserProfileEntries.Result {
            INSTANCE
        }

        record Success(PublicUserProfileResponse userProfileResponse) implements FindPublicUserProfileEntries.Result {
        }
    }
}
