package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.facade.RelationFacade;
import ru.qwonix.test.social.media.api.mapper.FriendMapper;
import ru.qwonix.test.social.media.api.result.AddFriendEntries;
import ru.qwonix.test.social.media.api.result.RemoveFriendEntries;
import ru.qwonix.test.social.media.api.serivce.RelationService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

@RequiredArgsConstructor
@Component
public class RelationFacadeImpl implements RelationFacade {

    private final RelationService relationService;
    private final UserProfileService userProfileService;
    private final FriendMapper friendMapper;

    @Override
    public AddFriendEntries.Result addFriend(String sourceUsername, String targetUsername) {
        var optionalSource = userProfileService.findUserByUsername(sourceUsername);
        if (optionalSource.isPresent()) {
            var optionalTarget = userProfileService.findUserByUsername(targetUsername);
            if (optionalTarget.isPresent()) {
                var source = optionalSource.get();
                var target = optionalTarget.get();

                if (Boolean.TRUE.equals(relationService.isFriends(source, target))) {
                    return AddFriendEntries.Result.UsersAreAlreadyFriends.INSTANCE;
                }

                if (Boolean.TRUE.equals(relationService.isSubscriber(source, target))) {
                    return AddFriendEntries.Result.RepeatedRequest.INSTANCE;
                }

                relationService.addSubscriber(source, target);
                return AddFriendEntries.Result.Success.INSTANCE;
            } else {
                return AddFriendEntries.Result.UserNotFound.INSTANCE;
            }
        } else {
            return AddFriendEntries.Result.UserNotFound.INSTANCE;
        }
    }

    @Override
    public RemoveFriendEntries.Result removeFriend(String sourceUsername, String targetUsername) {
        var optionalSource = userProfileService.findUserByUsername(sourceUsername);
        if (optionalSource.isPresent()) {
            var optionalTarget = userProfileService.findUserByUsername(targetUsername);
            if (optionalTarget.isPresent()) {
                var source = optionalSource.get();
                var target = optionalTarget.get();

                if (Boolean.FALSE.equals(relationService.isFriends(source, target))) {
                    return RemoveFriendEntries.Result.UsersAreNotFriends.INSTANCE;
                }

                relationService.removeSubscriber(source, target);
                return RemoveFriendEntries.Result.Success.INSTANCE;
            } else {
                return RemoveFriendEntries.Result.UserNotFound.INSTANCE;
            }
        } else {
            return RemoveFriendEntries.Result.UserNotFound.INSTANCE;
        }

    }
}
