package ru.qwonix.test.social.media.api.dto;

import jakarta.validation.constraints.NotEmpty;

public record SendMessageRequest(

        @NotEmpty(message = "the text cannot be blank")
        String text
) {
}
