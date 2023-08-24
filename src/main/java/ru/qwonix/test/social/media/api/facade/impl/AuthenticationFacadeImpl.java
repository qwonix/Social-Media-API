package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.dto.AuthenticationResponse;
import ru.qwonix.test.social.media.api.dto.UserRegistrationRequest;
import ru.qwonix.test.social.media.api.entity.Token;
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
    private final UserProfileService userProfileService;
    private final UserDetailsService userDetailsService;
    private final UserProfileMapper userProfileMapper;


    @Override
    public RegisterUserEntries.Result registerUser(UserRegistrationRequest registrationRequest) {
        if (userProfileService.existsByUsername(registrationRequest.username())) {
            return RegisterUserEntries.Result.UsernameAlreadyExists.INSTANCE;
        }
        if (userProfileService.existsByEmail(registrationRequest.email())) {
            return RegisterUserEntries.Result.EmailAlreadyExists.INSTANCE;
        }
        var registrant = userProfileMapper.map(registrationRequest);
        var registeredUser = userProfileService.register(registrant);

        return new RegisterUserEntries.Result.Success(userProfileMapper.mapToFull(registeredUser));
    }

    @Override
    public GenerateTokenEntries.Result generateAuthenticationToken(String username) {
        var userDetails = userDetailsService.loadUserByUsername(username);

        var token = authenticationService.serializeToken(new Token(userDetails.getUsername(), userDetails.getAuthorities()));
        return new GenerateTokenEntries.Result.Success(new AuthenticationResponse(token));
    }
}
