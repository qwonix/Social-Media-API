package ru.qwonix.test.social.media.api.result;


public class AddFriendEntries {
    public sealed interface Result {

        enum UserNotFound implements AddFriendEntries.Result {
            INSTANCE
        }

        enum UsersAreAlreadyFriends implements AddFriendEntries.Result {
            INSTANCE
        }

        enum RepeatedRequest implements AddFriendEntries.Result {
            INSTANCE
        }

        enum Success implements AddFriendEntries.Result {
            INSTANCE
        }
    }
}
