package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponseDto;

public class CreatePostEntries {
    public sealed interface Result {
        enum PostOwnerNotFound implements CreatePostEntries.Result {
            INSTANCE
        }

        record Success(PostResponseDto postResponseDto) implements CreatePostEntries.Result {
        }
    }
}
