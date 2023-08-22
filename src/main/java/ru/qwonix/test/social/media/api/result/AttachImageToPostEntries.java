package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponse;

public class AttachImageToPostEntries {
    public sealed interface Result {

        enum PostNotFound implements AttachImageToPostEntries.Result {
            INSTANCE
        }

        enum ImageNotFound implements AttachImageToPostEntries.Result {
            INSTANCE
        }

        enum ImageAlreadyAttached implements AttachImageToPostEntries.Result {
            INSTANCE
        }

        record Success(PostResponse postResponse) implements AttachImageToPostEntries.Result {

        }
    }
}
