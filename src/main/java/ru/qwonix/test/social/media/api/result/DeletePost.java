package ru.qwonix.test.social.media.api.result;

public class DeletePost {
    public sealed interface Result {

        enum NotFound implements DeletePost.Result {
            INSTANCE
        }

        enum Success implements DeletePost.Result {
            INSTANCE
        }
    }
}
