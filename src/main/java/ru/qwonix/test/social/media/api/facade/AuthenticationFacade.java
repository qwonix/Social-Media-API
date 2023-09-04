package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.UserRegistrationRequest;
import ru.qwonix.test.social.media.api.result.GenerateToken;
import ru.qwonix.test.social.media.api.result.RegisterUser;

public interface AuthenticationFacade {
    RegisterUser.Result registerUser(UserRegistrationRequest registrationRequest);

    GenerateToken.Result generateAuthenticationToken(String username);
}
