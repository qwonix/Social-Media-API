package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.Feed;

public class GetFeedEntries {
    public sealed interface Result {

        enum UserNotFound implements GetFeedEntries.Result {
            INSTANCE
        }

        record Success(Feed feed) implements GetFeedEntries.Result {
        }
    }
}
