package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.UserRegistrationRequest;
import ru.qwonix.test.social.media.api.result.GenerateTokenEntries;
import ru.qwonix.test.social.media.api.result.RegisterUserEntries;

public interface AuthenticationFacade {
    RegisterUserEntries.Result registerUser(UserRegistrationRequest registrationRequest);

    GenerateTokenEntries.Result getAuthenticationToken(String username);
}
