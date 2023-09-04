package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.facade.UserProfileFacade;
import ru.qwonix.test.social.media.api.mapper.UserProfileMapper;
import ru.qwonix.test.social.media.api.result.FindFullUserProfile;
import ru.qwonix.test.social.media.api.result.FindPublicUserProfile;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;


@RequiredArgsConstructor
@Component
public class UserProfileFacadeImpl implements UserProfileFacade {

    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @Override
    public FindFullUserProfile.Result findFullByUsername(String username) {
        var optionalUserProfile = userProfileService.findUserByUsername(username);
        if (optionalUserProfile.isEmpty()) {
            return FindFullUserProfile.Result.NotFound.INSTANCE;
        }
        var userProfile = optionalUserProfile.get();

        return new FindFullUserProfile.Result.Success(userProfileMapper.mapToFull(userProfile));
    }

    @Override
    public FindPublicUserProfile.Result findPublicByUsername(String username) {
        var optionalUserProfile = userProfileService.findUserByUsername(username);
        if (optionalUserProfile.isEmpty()) {
            return FindPublicUserProfile.Result.NotFound.INSTANCE;
        }
        var userProfile = optionalUserProfile.get();

        return new FindPublicUserProfile.Result.Success(userProfileMapper.mapToPublic(userProfile));
    }
}
