package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.ImageResourceResponse;

public class FindImage {
    public sealed interface Result {
        enum NotFound implements FindImage.Result {
            INSTANCE
        }

        record Success(ImageResourceResponse imageResourceResponse) implements FindImage.Result {
        }
    }
}
