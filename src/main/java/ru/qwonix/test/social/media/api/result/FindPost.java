package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponse;

public class FindPost {
    public sealed interface Result {
        enum NotFound implements FindPost.Result {
            INSTANCE
        }

        record Success(PostResponse postResponse) implements FindPost.Result {
        }
    }
}
