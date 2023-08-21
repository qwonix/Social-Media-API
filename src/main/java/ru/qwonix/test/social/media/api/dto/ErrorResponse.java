package ru.qwonix.test.social.media.api.dto;

import java.util.List;
import java.util.Map;

public record ErrorResponse(List<ErrorMessage> errorMessages) {
    public ErrorResponse(Map<String, String> errorMessages) {
        this(errorMessages.entrySet().stream().map(stringStringEntry -> new ErrorMessage(stringStringEntry.getKey(), stringStringEntry.getValue())).toList());
    }

    public ErrorResponse(String field, String message) {
        this(List.of(new ErrorMessage(field, message)));
    }
}
