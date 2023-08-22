package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.FeedDto;
import ru.qwonix.test.social.media.api.dto.MessageDto;

import java.util.List;

public class GetFeedEntries {
    public sealed interface Result {

        enum UserNotFound implements GetFeedEntries.Result {
            INSTANCE
        }

        record Success(FeedDto feed) implements GetFeedEntries.Result {
        }
    }
}
