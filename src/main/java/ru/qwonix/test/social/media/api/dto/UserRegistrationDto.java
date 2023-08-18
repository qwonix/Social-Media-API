package ru.qwonix.test.social.media.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(
        @Size(min = 3, max = 20, message = "username must be longer than 3 and shorter than 20 characters")
        String username,
        @NotEmpty @Email
        String email,
        @NotEmpty @Size(min = 8, message = "password should have at least 8 characters")
        String password
) {
}