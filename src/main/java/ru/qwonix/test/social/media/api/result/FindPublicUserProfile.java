package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PublicUserProfileResponse;

public class FindPublicUserProfile {
    public sealed interface Result {
        enum NotFound implements FindPublicUserProfile.Result {
            INSTANCE
        }

        record Success(PublicUserProfileResponse userProfileResponse) implements FindPublicUserProfile.Result {
        }
    }
}
