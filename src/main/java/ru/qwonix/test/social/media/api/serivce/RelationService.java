package ru.qwonix.test.social.media.api.serivce;

import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.List;

public interface RelationService {

    /**
     * Retrieves a list of user profiles that the specified user is subscribed to
     *
     * @param userProfile user profile for whom to retrieve subscriptions
     * @return list of user profiles that the specified user is subscribed to
     */
    List<UserProfile> findAllSubscriptions(UserProfile userProfile);

    /**
     * Checks if the source user is a subscriber of the target user
     *
     * @param source source user profile
     * @param target target user profile
     * @return {@code true} if the source user is a subscriber of the target user, {@code false} otherwise
     */
    Boolean isSubscriber(UserProfile source, UserProfile target);

    /**
     * Checks if the two specified users are friends
     *
     * @return {@code true} if the two users are friends, {@code false} otherwise
     */
    Boolean isFriends(UserProfile user1, UserProfile user2);

    /**
     * Adds a subscription from the source user to the target user
     *
     * @param source source user profile
     * @param target target user profile
     */
    void addSubscriber(UserProfile source, UserProfile target);

    /**
     * Removes a subscription from the source user to the target user
     *
     * @param source source user profile
     * @param target target user profile
     */
    void removeSubscriber(UserProfile source, UserProfile target);
}