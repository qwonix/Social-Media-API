package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponse;

public class UpdatePost {
    public sealed interface Result {

        enum NotFound implements UpdatePost.Result {
            INSTANCE
        }

        record Success(PostResponse postResponse) implements UpdatePost.Result {
        }
    }
}
