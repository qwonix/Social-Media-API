package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.facade.UserProfileFacade;
import ru.qwonix.test.social.media.api.mapper.UserProfileMapper;
import ru.qwonix.test.social.media.api.result.FindFullUserProfileEntries;
import ru.qwonix.test.social.media.api.result.FindPublicUserProfileEntries;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;


@RequiredArgsConstructor
@Component
public class UserProfileFacadeImpl implements UserProfileFacade {

    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @Override
    public FindFullUserProfileEntries.Result findFullByUsername(String username) {
        var optionalUserProfile = userProfileService.findUserByUsername(username);
        if (optionalUserProfile.isEmpty()) {
            return FindFullUserProfileEntries.Result.NotFound.INSTANCE;
        }

        var userProfile = optionalUserProfile.get();
        return new FindFullUserProfileEntries.Result.Success(userProfileMapper.mapToFull(userProfile));
    }

    @Override
    public FindPublicUserProfileEntries.Result findPublicByUsername(String username) {
        var optionalUserProfile = userProfileService.findUserByUsername(username);
        if (optionalUserProfile.isEmpty()) {
            return FindPublicUserProfileEntries.Result.NotFound.INSTANCE;
        }
        var userProfile = optionalUserProfile.get();

        return new FindPublicUserProfileEntries.Result.Success(userProfileMapper.mapToPublic(userProfile));
    }
}
