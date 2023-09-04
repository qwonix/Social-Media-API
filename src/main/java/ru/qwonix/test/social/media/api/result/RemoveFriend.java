package ru.qwonix.test.social.media.api.result;


public class RemoveFriend {
    public sealed interface Result {

        enum UserNotFound implements RemoveFriend.Result {
            INSTANCE
        }

        enum UsersAreNotFriends implements RemoveFriend.Result {
            INSTANCE
        }

        enum Success implements RemoveFriend.Result {
            INSTANCE
        }
    }
}
