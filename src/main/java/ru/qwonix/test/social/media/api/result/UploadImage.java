package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.ImageResponse;

public class UploadImage {
    public sealed interface Result {
        enum UserNotFound implements UploadImage.Result {
            INSTANCE
        }

        enum FileIsNotImage implements UploadImage.Result {
            INSTANCE
        }

        record Success(ImageResponse imageResponse) implements UploadImage.Result {
        }
    }
}
