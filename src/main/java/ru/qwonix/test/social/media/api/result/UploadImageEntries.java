package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.ImageResponse;

public class UploadImageEntries {
    public sealed interface Result {
        enum UserNotFound implements UploadImageEntries.Result {
            INSTANCE
        }

        enum FileIsNotImage implements UploadImageEntries.Result {
            INSTANCE
        }

        record Success(ImageResponse imageResponse) implements UploadImageEntries.Result {
        }
    }
}
