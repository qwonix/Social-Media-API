package ru.qwonix.test.social.media.api.result;

import ru.qwonix.test.social.media.api.dto.PostResponseDto;

public class FindPostEntries {
    public sealed interface Result {
        enum NotFound implements FindPostEntries.Result {
            INSTANCE
        }

        record Success(PostResponseDto postResponseDto) implements FindPostEntries.Result {
        }
    }
}
