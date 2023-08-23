package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.dto.AuthenticationResponse;
import ru.qwonix.test.social.media.api.dto.UserRegistrationRequest;
import ru.qwonix.test.social.media.api.entity.Token;
import ru.qwonix.test.social.media.api.entity.UserProfile;
import ru.qwonix.test.social.media.api.facade.AuthenticationFacade;
import ru.qwonix.test.social.media.api.mapper.UserProfileMapper;
import ru.qwonix.test.social.media.api.result.GenerateTokenEntries;
import ru.qwonix.test.social.media.api.result.RegisterUserEntries;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

@RequiredArgsConstructor
@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final AuthenticationService authenticationService;
    private final UserProfileService userDetailsService;
    private final UserProfileMapper userProfileMapper;


    @Override
    public RegisterUserEntries.Result registerUser(UserRegistrationRequest registrationRequest) {
        String username = registrationRequest.username();
        if (Boolean.TRUE.equals(userDetailsService.existsByUsername(username))) {
            return RegisterUserEntries.Result.UsernameAlreadyExists.INSTANCE;
        }
        String email = registrationRequest.email();
        if (Boolean.TRUE.equals(userDetailsService.existsByEmail(email))) {
            return RegisterUserEntries.Result.EmailAlreadyExists.INSTANCE;
        }
        UserProfile user = userProfileMapper.map(registrationRequest);
        UserProfile userProfile = userDetailsService.register(user);

        return new RegisterUserEntries.Result.Success(userProfileMapper.mapToFull(userProfile));
    }

    @Override
    public GenerateTokenEntries.Result generateAuthenticationToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        try {
            String token = authenticationService.serializeToken(new Token(userDetails.getUsername(), userDetails.getAuthorities()));
            return new GenerateTokenEntries.Result.Success(new AuthenticationResponse(token));
        } catch (Exception e) {
            return new GenerateTokenEntries.Result.Fail(e.getMessage());
        }
    }
}
