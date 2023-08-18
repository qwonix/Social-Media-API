package ru.qwonix.test.social.media.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.qwonix.test.social.media.api.dto.AuthenticationResponse;
import ru.qwonix.test.social.media.api.dto.ErrorMessage;
import ru.qwonix.test.social.media.api.dto.ErrorResponse;
import ru.qwonix.test.social.media.api.dto.UserRegistrationDto;
import ru.qwonix.test.social.media.api.facade.AuthenticationFacade;
import ru.qwonix.test.social.media.api.result.RegisterUserEntries;
import ru.qwonix.test.social.media.api.result.TokenGenerationEntries;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationDto registrationDto) {
        log.debug("Registration request with username {}", registrationDto.username());
        var result = authenticationFacade.registerUser(registrationDto);
        if (result instanceof RegisterUserEntries.Result.UsernameAlreadyExists) {
            log.debug("Registration request fail user already exists {}", registrationDto.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(List.of(new ErrorMessage("username", "username already exists"))));
        } else if (result instanceof RegisterUserEntries.Result.EmailAlreadyExists) {
            log.debug("Registration request fail email already exists {}", registrationDto.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(List.of(new ErrorMessage("email", "email already exists"))));
        } else if (result instanceof RegisterUserEntries.Result.Success) {
            log.debug("Registration request success {}", registrationDto.username());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> auth(@AuthenticationPrincipal UserDetails user) {
        log.debug("Authentication request with username {}", user.getUsername());
        var result = authenticationFacade.getAuthenticationToken(user.getUsername());
        if (result instanceof TokenGenerationEntries.Result.Success success) {
            log.debug("Authentication request success");
            var token = success.token();
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

}
