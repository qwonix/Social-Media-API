package ru.qwonix.test.social.media.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.qwonix.test.social.media.api.facade.UserProfileFacade;
import ru.qwonix.test.social.media.api.result.FindFullUserProfileEntries;
import ru.qwonix.test.social.media.api.result.FindPublicUserProfileEntries;

@Tag(name = "User", description = "User Profile endpoints")
@SecurityRequirement(name = "Bearer")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/profile")
public class UserController {

    private final UserProfileFacade userProfileFacade;

    @Operation(summary = "Get a user's profile by username", responses = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{username}")
    public ResponseEntity<?> get(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String username) {
        log.debug("Get user with username {}", username);
        if (username.equals(userDetails.getUsername())) {
            var result = userProfileFacade.findFullByUsername(username);
            if (result instanceof FindFullUserProfileEntries.Result.NotFound) {
                log.debug("Error retrieving user, no username {}", username);
                return ResponseEntity.notFound().build();

            } else if (result instanceof FindFullUserProfileEntries.Result.Success successResult) {
                log.debug("Success user receipt with username {}", username);
                return ResponseEntity.ok(successResult.userProfileResponse());
            }

        } else {
            var result = userProfileFacade.findPublicByUsername(username);
            if (result instanceof FindPublicUserProfileEntries.Result.NotFound) {
                log.debug("Error retrieving user, no username {}", username);
                return ResponseEntity.notFound().build();

            } else if (result instanceof FindPublicUserProfileEntries.Result.Success successResult) {
                log.debug("Success user receipt with username {}", username);
                return ResponseEntity.ok(successResult.userProfileResponse());
            }
        }

        return ResponseEntity.internalServerError().build();
    }
}
