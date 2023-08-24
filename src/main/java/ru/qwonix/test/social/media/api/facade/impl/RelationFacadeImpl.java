package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.facade.RelationFacade;
import ru.qwonix.test.social.media.api.result.AddFriendEntries;
import ru.qwonix.test.social.media.api.result.RemoveFriendEntries;
import ru.qwonix.test.social.media.api.serivce.RelationService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

@RequiredArgsConstructor
@Component
public class RelationFacadeImpl implements RelationFacade {

    private final RelationService relationService;
    private final UserProfileService userProfileService;

    @Override
    public AddFriendEntries.Result addFriend(String sourceUsername, String targetUsername) {
        var optionalSource = userProfileService.findUserByUsername(sourceUsername);
        if (optionalSource.isEmpty()) {
            return AddFriendEntries.Result.UserNotFound.INSTANCE;
        }
        var optionalTarget = userProfileService.findUserByUsername(targetUsername);
        if (optionalTarget.isEmpty()) {
            return AddFriendEntries.Result.UserNotFound.INSTANCE;
        }
        var source = optionalSource.get();
        var target = optionalTarget.get();

        if (relationService.isFriends(source, target)) {
            return AddFriendEntries.Result.UsersAreAlreadyFriends.INSTANCE;
        }

        if (relationService.isSubscriber(source, target)) {
            return AddFriendEntries.Result.RepeatedRequest.INSTANCE;
        }

        relationService.subscribe(source, target);
        return AddFriendEntries.Result.Success.INSTANCE;
    }

    @Override
    public RemoveFriendEntries.Result removeFriend(String sourceUsername, String targetUsername) {
        var optionalSource = userProfileService.findUserByUsername(sourceUsername);
        if (optionalSource.isEmpty()) {
            return RemoveFriendEntries.Result.UserNotFound.INSTANCE;
        }
        var optionalTarget = userProfileService.findUserByUsername(targetUsername);
        if (optionalTarget.isEmpty()) {
            return RemoveFriendEntries.Result.UserNotFound.INSTANCE;
        }

        var source = optionalSource.get();
        var target = optionalTarget.get();

        if (!relationService.isFriends(source, target)) {
            return RemoveFriendEntries.Result.UsersAreNotFriends.INSTANCE;
        }

        relationService.unsubscribe(source, target);
        return RemoveFriendEntries.Result.Success.INSTANCE;
    }
}
