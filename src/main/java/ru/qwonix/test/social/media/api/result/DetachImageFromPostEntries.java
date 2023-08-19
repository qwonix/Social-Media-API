package ru.qwonix.test.social.media.api.result;

public class DetachImageFromPostEntries {
    public sealed interface Result {

        enum PostNotFound implements DetachImageFromPostEntries.Result {
            INSTANCE
        }

        enum ImageNotFound implements DetachImageFromPostEntries.Result {
            INSTANCE
        }

        enum ImageAlreadyDetached implements DetachImageFromPostEntries.Result {
            INSTANCE
        }

        enum Success implements DetachImageFromPostEntries.Result {
            INSTANCE
        }
    }
}
