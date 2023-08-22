package ru.qwonix.test.social.media.api.serivce;

import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.List;

public interface RelationService {

    List<UserProfile> findAllSubscriptions(UserProfile userProfile);

    Boolean isSubscriber(UserProfile source, UserProfile target);

    Boolean isFriends(UserProfile user1, UserProfile user2);

    void addSubscriber(UserProfile source, UserProfile target);

    void removeSubscriber(UserProfile source, UserProfile target);
}
