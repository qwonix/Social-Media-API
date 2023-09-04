package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponse;

public class CreatePost {
    public sealed interface Result {
        enum PostOwnerNotFound implements CreatePost.Result {
            INSTANCE
        }

        record Success(PostResponse postResponse) implements CreatePost.Result {
        }
    }
}
