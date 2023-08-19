package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponseDto;

public class UpdatePostEntries {
    public sealed interface Result {

        enum NotFound implements UpdatePostEntries.Result {
            INSTANCE
        }

        record Success(PostResponseDto postResponseDto) implements UpdatePostEntries.Result {
        }
    }
}
