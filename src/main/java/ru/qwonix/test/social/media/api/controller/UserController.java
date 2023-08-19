package ru.qwonix.test.social.media.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.qwonix.test.social.media.api.facade.UserProfileFacade;
import ru.qwonix.test.social.media.api.result.FindFullUserProfileEntries;
import ru.qwonix.test.social.media.api.result.FindPublicUserProfileEntries;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/profile")
public class UserController {

    private final UserProfileFacade userProfileFacade;

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
                return ResponseEntity.ok(successResult.userProfileResponseDto());
            }

        } else {
            var result = userProfileFacade.findPublicByUsername(username);
            if (result instanceof FindPublicUserProfileEntries.Result.NotFound) {
                log.debug("Error retrieving user, no username {}", username);
                return ResponseEntity.notFound().build();

            } else if (result instanceof FindPublicUserProfileEntries.Result.Success successResult) {
                log.debug("Success user receipt with username {}", username);
                return ResponseEntity.ok(successResult.userProfileResponseDto());
            }
        }

        return ResponseEntity.internalServerError().build();
    }
}
