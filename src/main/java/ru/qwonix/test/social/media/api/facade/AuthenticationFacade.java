package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.UserRegistrationDto;
import ru.qwonix.test.social.media.api.result.GenerateTokenEntries;
import ru.qwonix.test.social.media.api.result.RegisterUserEntries;

public interface AuthenticationFacade {
    RegisterUserEntries.Result registerUser(UserRegistrationDto registrationDto);

    GenerateTokenEntries.Result getAuthenticationToken(String username);
}
