package ru.qwonix.test.social.media.api.result;


import ru.qwonix.test.social.media.api.dto.ImageResourceResponse;

public class FindImageEntries {
    public sealed interface Result {
        enum NotFound implements FindImageEntries.Result {
            INSTANCE
        }

        record Success(ImageResourceResponse imageResourceResponse) implements FindImageEntries.Result {
        }
    }
}
