package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponse;

public class CreatePostEntries {
    public sealed interface Result {
        enum PostOwnerNotFound implements CreatePostEntries.Result {
            INSTANCE
        }

        record Success(PostResponse postResponse) implements CreatePostEntries.Result {
        }
    }
}
