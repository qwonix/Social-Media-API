package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.result.FindFullUserProfileEntries;
import ru.qwonix.test.social.media.api.result.FindPublicUserProfileEntries;

public interface UserProfileFacade {
    FindFullUserProfileEntries.Result findFullByUsername(String username);

    FindPublicUserProfileEntries.Result findPublicByUsername(String username);
}
