package ru.qwonix.test.social.media.api.result;


public class AddFriend {
    public sealed interface Result {

        enum UserNotFound implements AddFriend.Result {
            INSTANCE
        }

        enum UsersAreAlreadyFriends implements AddFriend.Result {
            INSTANCE
        }

        enum RepeatedRequest implements AddFriend.Result {
            INSTANCE
        }

        enum Success implements AddFriend.Result {
            INSTANCE
        }
    }
}
