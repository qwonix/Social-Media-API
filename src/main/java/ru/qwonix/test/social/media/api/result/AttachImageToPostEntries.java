package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponseDto;

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

        record Success(PostResponseDto postResponseDto) implements AttachImageToPostEntries.Result {

        }
    }
}
