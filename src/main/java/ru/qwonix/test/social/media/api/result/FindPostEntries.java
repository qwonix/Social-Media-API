package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponse;

public class FindPostEntries {
    public sealed interface Result {
        enum NotFound implements FindPostEntries.Result {
            INSTANCE
        }

        record Success(PostResponse postResponse) implements FindPostEntries.Result {
        }
    }
}
