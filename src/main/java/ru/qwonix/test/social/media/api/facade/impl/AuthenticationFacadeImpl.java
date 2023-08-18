package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.dto.UserRegistrationDto;
import ru.qwonix.test.social.media.api.entity.UserProfile;
import ru.qwonix.test.social.media.api.facade.AuthenticationFacade;
import ru.qwonix.test.social.media.api.mapper.UserProfileMapper;
import ru.qwonix.test.social.media.api.result.RegisterUserEntries;
import ru.qwonix.test.social.media.api.result.TokenGenerationEntries;
import ru.qwonix.test.social.media.api.serivce.AuthenticationService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

@RequiredArgsConstructor
@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final AuthenticationService authenticationService;
    private final UserProfileService userDetailsService;
    private final UserProfileMapper userProfileMapper;


    @Override
    public RegisterUserEntries.Result registerUser(UserRegistrationDto registrationDto) {
        UserProfile user = userProfileMapper.map(registrationDto);
        userDetailsService.register(user);
        return RegisterUserEntries.Result.Success.INSTANCE;
    }

    @Override
    public TokenGenerationEntries.Result getAuthenticationToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        try {
            String token = authenticationService.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
            return new TokenGenerationEntries.Result.Success(token);
        } catch (Exception e) {
            return new TokenGenerationEntries.Result.Fail(e.getMessage());
        }
    }
}
