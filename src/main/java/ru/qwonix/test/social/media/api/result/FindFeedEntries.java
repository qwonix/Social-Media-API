package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.Feed;

public class FindFeedEntries {
    public sealed interface Result {

        enum UserNotFound implements FindFeedEntries.Result {
            INSTANCE
        }

        record Success(Feed feed) implements FindFeedEntries.Result {
        }
    }
}
