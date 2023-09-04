package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponse;

public class DetachImageFromPost {
    public sealed interface Result {

        enum PostNotFound implements DetachImageFromPost.Result {
            INSTANCE
        }

        enum ImageNotFound implements DetachImageFromPost.Result {
            INSTANCE
        }

        enum ImageNotAttached implements DetachImageFromPost.Result {
            INSTANCE
        }

        record Success(PostResponse postResponse) implements DetachImageFromPost.Result {

        }
    }
}
