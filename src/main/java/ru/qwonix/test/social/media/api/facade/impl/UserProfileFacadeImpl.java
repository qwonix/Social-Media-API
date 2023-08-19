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
        if (optionalUserProfile.isPresent()) {
            var userProfile = optionalUserProfile.get();
            var fullUserDto = userProfileMapper.mapToFull(userProfile);

            return new FindFullUserProfileEntries.Result.Success(fullUserDto);
        } else {
            return FindFullUserProfileEntries.Result.NotFound.INSTANCE;
        }
    }

    @Override
    public FindPublicUserProfileEntries.Result findPublicByUsername(String username) {
        var optionalUserProfile = userProfileService.findUserByUsername(username);
        if (optionalUserProfile.isPresent()) {
            var userProfile = optionalUserProfile.get();
            var publicUserDto = userProfileMapper.mapToPublic(userProfile);

            return new FindPublicUserProfileEntries.Result.Success(publicUserDto);
        } else {
            return FindPublicUserProfileEntries.Result.NotFound.INSTANCE;
        }
    }
}
