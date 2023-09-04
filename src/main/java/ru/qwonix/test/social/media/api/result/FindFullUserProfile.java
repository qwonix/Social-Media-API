package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.FullUserProfileResponse;

public class FindFullUserProfile {
    public sealed interface Result {
        enum NotFound implements FindFullUserProfile.Result {
            INSTANCE
        }

        record Success(FullUserProfileResponse userProfileResponse) implements FindFullUserProfile.Result {
        }
    }
}
