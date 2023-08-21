package ru.qwonix.test.social.media.api.result;


public class RemoveFriendEntries {
    public sealed interface Result {

        enum UserNotFound implements RemoveFriendEntries.Result {
            INSTANCE
        }

        enum UsersAreNotFriends implements RemoveFriendEntries.Result {
            INSTANCE
        }

        enum Success implements RemoveFriendEntries.Result {
            INSTANCE
        }
    }
}
