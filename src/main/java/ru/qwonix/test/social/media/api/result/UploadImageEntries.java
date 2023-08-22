package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.ImageResponseDto;

public class UploadImageEntries {
    public sealed interface Result {
        enum UserNotFound implements UploadImageEntries.Result {
            INSTANCE
        }

        enum FileIsNotImage implements UploadImageEntries.Result {
            INSTANCE
        }

        record Success(ImageResponseDto imageResponseDto) implements UploadImageEntries.Result {
        }
    }
}
