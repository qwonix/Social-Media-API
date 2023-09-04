package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.facade.RelationFacade;
import ru.qwonix.test.social.media.api.result.AddFriend;
import ru.qwonix.test.social.media.api.result.RemoveFriend;
import ru.qwonix.test.social.media.api.serivce.RelationService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

@RequiredArgsConstructor
@Component
public class RelationFacadeImpl implements RelationFacade {

    private final RelationService relationService;
    private final UserProfileService userProfileService;

    @Override
    public AddFriend.Result addFriend(String sourceUsername, String targetUsername) {
        var optionalSource = userProfileService.findUserByUsername(sourceUsername);
        if (optionalSource.isEmpty()) {
            return AddFriend.Result.UserNotFound.INSTANCE;
        }
        var optionalTarget = userProfileService.findUserByUsername(targetUsername);
        if (optionalTarget.isEmpty()) {
            return AddFriend.Result.UserNotFound.INSTANCE;
        }
        var source = optionalSource.get();
        var target = optionalTarget.get();

        if (relationService.areFriends(source, target)) {
            return AddFriend.Result.UsersAreAlreadyFriends.INSTANCE;
        }

        if (relationService.isSubscriber(source, target)) {
            return AddFriend.Result.RepeatedRequest.INSTANCE;
        }

        relationService.subscribe(source, target);
        return AddFriend.Result.Success.INSTANCE;
    }

    @Override
    public RemoveFriend.Result removeFriend(String sourceUsername, String targetUsername) {
        var optionalSource = userProfileService.findUserByUsername(sourceUsername);
        if (optionalSource.isEmpty()) {
            return RemoveFriend.Result.UserNotFound.INSTANCE;
        }
        var optionalTarget = userProfileService.findUserByUsername(targetUsername);
        if (optionalTarget.isEmpty()) {
            return RemoveFriend.Result.UserNotFound.INSTANCE;
        }

        var source = optionalSource.get();
        var target = optionalTarget.get();

        if (relationService.areNotFriends(source, target)) {
            return RemoveFriend.Result.UsersAreNotFriends.INSTANCE;
        }

        relationService.unsubscribe(source, target);
        return RemoveFriend.Result.Success.INSTANCE;
    }
}
