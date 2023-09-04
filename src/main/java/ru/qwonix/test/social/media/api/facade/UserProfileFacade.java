package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.result.FindFullUserProfile;
import ru.qwonix.test.social.media.api.result.FindPublicUserProfile;

public interface UserProfileFacade {
    FindFullUserProfile.Result findFullByUsername(String username);

    FindPublicUserProfile.Result findPublicByUsername(String username);
}
