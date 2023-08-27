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
    boolean isSubscriber(UserProfile source, UserProfile target);

    /**
     * Checks if the two specified users are not friends
     *
     * @return {@code true} if users are not friends, {@code false} otherwise
     */
    boolean areNotFriends(UserProfile user1, UserProfile user2);

    /**
     * Checks if the two specified users are friends
     *
     * @return {@code true} if users are friends, {@code false} otherwise
     */
    boolean areFriends(UserProfile user1, UserProfile user2);

    /**
     * Subscribes the source user to the target user
     *
     * @param source the user who subscribes
     * @param target the user to subscribe to
     */
    void subscribe(UserProfile source, UserProfile target);

    /**
     * Unsubscribes the source user from the target user
     *
     * @param source the user who unsubscribes
     * @param target the user to unsubscribe from
     */
    void unsubscribe(UserProfile source, UserProfile target);
}