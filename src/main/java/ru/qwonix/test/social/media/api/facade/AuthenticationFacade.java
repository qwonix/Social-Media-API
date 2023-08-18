package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.UserRegistrationDto;
import ru.qwonix.test.social.media.api.result.RegisterUserEntries;
import ru.qwonix.test.social.media.api.result.TokenGenerationEntries;

public interface AuthenticationFacade {
    RegisterUserEntries.Result registerUser(UserRegistrationDto registrationDto);

    TokenGenerationEntries.Result getAuthenticationToken(String username);
}
