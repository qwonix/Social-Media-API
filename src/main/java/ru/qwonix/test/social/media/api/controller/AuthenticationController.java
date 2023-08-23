package ru.qwonix.test.social.media.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.qwonix.test.social.media.api.dto.*;
import ru.qwonix.test.social.media.api.facade.AuthenticationFacade;
import ru.qwonix.test.social.media.api.result.GenerateTokenEntries;
import ru.qwonix.test.social.media.api.result.RegisterUserEntries;

import java.util.List;
import java.util.Map;

@Tag(name = "Authentication", description = "Authentication and registration endpoints")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    public static final ErrorResponse EMAIL_ALREADY_EXISTS = new ErrorResponse(List.of(new ErrorMessage("email", "email already exists")));
    public static final ErrorResponse USERNAME_ALREADY_EXISTS = new ErrorResponse(List.of(new ErrorMessage("username", "username already exists")));

    private final AuthenticationFacade authenticationFacade;


    @Operation(summary = "Registration", responses = {
            @ApiResponse(responseCode = "201", description = "User successfully registered", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FullUserProfileResponse.class))
            }),
            @ApiResponse(responseCode = "409", description = "User registration conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(UriComponentsBuilder uriComponentsBuilder, @RequestBody @Valid UserRegistrationRequest registrationRequest) {
        log.debug("Registration request with username {}", registrationRequest.username());
        var result = authenticationFacade.registerUser(registrationRequest);

        if (result instanceof RegisterUserEntries.Result.UsernameAlreadyExists) {
            log.debug("Registration request fail user already exists {}", registrationRequest.username());

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(USERNAME_ALREADY_EXISTS);
        } else if (result instanceof RegisterUserEntries.Result.EmailAlreadyExists) {
            log.debug("Registration request fail email already exists {}", registrationRequest.username());

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(EMAIL_ALREADY_EXISTS);
        } else if (result instanceof RegisterUserEntries.Result.Success success) {
            log.debug("Registration request success {}", registrationRequest.username());

            return ResponseEntity.created(uriComponentsBuilder
                            .path("/api/v1/user/profile/{username}")
                            .build(Map.of("username", success.userProfileResponse().id())))
                    .body(success.userProfileResponse());
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Authenticate user and generate token", responses = {
            @ApiResponse(responseCode = "200", description = "User authenticated and token generated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))
            }),
    }, security = @SecurityRequirement(name = "Basic"))
    @GetMapping
    public ResponseEntity<AuthenticationResponse> auth(@AuthenticationPrincipal UserDetails user) {
        log.debug("Authentication request with username {}", user.getUsername());
        var result = authenticationFacade.getAuthenticationToken(user.getUsername());
        if (result instanceof GenerateTokenEntries.Result.Success success) {
            log.debug("Authentication request success");
            return ResponseEntity.ok(success.authenticationResponse());
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

}
