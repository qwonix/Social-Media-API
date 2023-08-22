package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponse;

public class DetachImageFromPostEntries {
    public sealed interface Result {

        enum PostNotFound implements DetachImageFromPostEntries.Result {
            INSTANCE
        }

        enum ImageNotFound implements DetachImageFromPostEntries.Result {
            INSTANCE
        }

        enum ImageNotAttached implements DetachImageFromPostEntries.Result {
            INSTANCE
        }

        record Success(PostResponse postResponse) implements DetachImageFromPostEntries.Result {

        }
    }
}
