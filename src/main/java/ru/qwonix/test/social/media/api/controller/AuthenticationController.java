package ru.qwonix.test.social.media.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.qwonix.test.social.media.api.dto.AuthenticationResponse;
import ru.qwonix.test.social.media.api.dto.ErrorMessage;
import ru.qwonix.test.social.media.api.dto.ErrorResponse;
import ru.qwonix.test.social.media.api.dto.UserRegistrationDto;
import ru.qwonix.test.social.media.api.facade.AuthenticationFacade;
import ru.qwonix.test.social.media.api.result.GenerateTokenEntries;
import ru.qwonix.test.social.media.api.result.RegisterUserEntries;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    public static final ErrorResponse EMAIL_ALREADY_EXISTS = new ErrorResponse(List.of(new ErrorMessage("email", "email already exists")));
    public static final ErrorResponse USERNAME_ALREADY_EXISTS = new ErrorResponse(List.of(new ErrorMessage("username", "username already exists")));

    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/register")
    public ResponseEntity<?> register(UriComponentsBuilder uriComponentsBuilder, @RequestBody @Valid UserRegistrationDto registrationDto) {
        log.debug("Registration request with username {}", registrationDto.username());
        var result = authenticationFacade.registerUser(registrationDto);
        if (result instanceof RegisterUserEntries.Result.UsernameAlreadyExists) {
            log.debug("Registration request fail user already exists {}", registrationDto.username());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(USERNAME_ALREADY_EXISTS);
        } else if (result instanceof RegisterUserEntries.Result.EmailAlreadyExists) {
            log.debug("Registration request fail email already exists {}", registrationDto.username());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(EMAIL_ALREADY_EXISTS);
        } else if (result instanceof RegisterUserEntries.Result.Success success) {
            log.debug("Registration request success {}", registrationDto.username());
            return ResponseEntity.created(uriComponentsBuilder
                            .path("/api/v1/user/profile/{username}")
                            .build(Map.of("username", success.userProfileResponseDto().id())))
                    .body(success.userProfileResponseDto());
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<AuthenticationResponse> auth(@AuthenticationPrincipal UserDetails user) {
        log.debug("Authentication request with username {}", user.getUsername());
        var result = authenticationFacade.getAuthenticationToken(user.getUsername());
        if (result instanceof GenerateTokenEntries.Result.Success success) {
            log.debug("Authentication request success");
            var token = success.token();
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

}
