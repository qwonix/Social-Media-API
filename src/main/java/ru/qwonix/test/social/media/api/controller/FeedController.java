package ru.qwonix.test.social.media.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.qwonix.test.social.media.api.dto.ErrorResponse;
import ru.qwonix.test.social.media.api.facade.FeedFacade;
import ru.qwonix.test.social.media.api.result.GetFeedEntries;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/feed")
public class FeedController {

    private final FeedFacade feedFacade;

    @GetMapping
    public ResponseEntity<?> getFeed(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam Optional<Integer> page,
                                     @RequestParam Optional<Integer> size) {
        log.debug("Get feed by {}", userDetails.getUsername());
        var result = feedFacade.getFeedPaginated(userDetails.getUsername(), page.orElse(0), size.orElse(10));

        if (result instanceof GetFeedEntries.Result.UserNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("user", "user doesn't exist"));
        } else if (result instanceof GetFeedEntries.Result.Success success) {
            return ResponseEntity.ok(success.feed().posts());
        }

        return ResponseEntity.internalServerError().build();
    }
}
