package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.result.AddFriendEntries;
import ru.qwonix.test.social.media.api.result.RemoveFriendEntries;

public interface RelationFacade {
    AddFriendEntries.Result addFriend(String sourceUsername, String targetUsername);

    RemoveFriendEntries.Result removeFriend(String sourceUsername, String targetUsername);
}
