package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.result.AddFriend;
import ru.qwonix.test.social.media.api.result.RemoveFriend;

public interface RelationFacade {
    AddFriend.Result addFriend(String sourceUsername, String targetUsername);

    RemoveFriend.Result removeFriend(String sourceUsername, String targetUsername);
}
