package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.Feed;

public class FindFeed {
    public sealed interface Result {

        enum UserNotFound implements FindFeed.Result {
            INSTANCE
        }

        record Success(Feed feed) implements FindFeed.Result {
        }
    }
}
