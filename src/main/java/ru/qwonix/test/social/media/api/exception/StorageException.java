package ru.qwonix.test.social.media.api.exception;

import io.jsonwebtoken.io.IOException;

public class StorageException extends IOException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
