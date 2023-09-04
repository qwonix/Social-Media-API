package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponse;

public class AttachImageToPost {
    public sealed interface Result {

        enum PostNotFound implements AttachImageToPost.Result {
            INSTANCE
        }

        enum ImageNotFound implements AttachImageToPost.Result {
            INSTANCE
        }

        enum ImageAlreadyAttached implements AttachImageToPost.Result {
            INSTANCE
        }

        record Success(PostResponse postResponse) implements AttachImageToPost.Result {

        }
    }
}
