package ru.qwonix.test.social.media.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.qwonix.test.social.media.api.dto.PostResponse;
import ru.qwonix.test.social.media.api.facade.FeedFacade;
import ru.qwonix.test.social.media.api.result.FindFeed;

import java.util.Optional;

@Tag(name = "Feed", description = "User activity feed endpoints")
@SecurityRequirement(name = "Bearer")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/feed")
public class FeedController {

    private final FeedFacade feedFacade;

    @Operation(summary = "Get user's feed (sorted by descending date – new first)", parameters = {
            @Parameter(name = "page", description = "Feed page"),
            @Parameter(name = "count", description = "Count of posts per page")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "User's feed retrieved successfully", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping
    public ResponseEntity<?> getFeed(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam Optional<Integer> page,
                                     @RequestParam Optional<Integer> count) {
        log.debug("Get feed by {}", userDetails.getUsername());
        var result = feedFacade.findFeedPaginated(userDetails.getUsername(), page.orElse(0), count.orElse(10));

        if (result instanceof FindFeed.Result.UserNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("user", "user doesn't exist"));
        } else if (result instanceof FindFeed.Result.Success success) {
            return ResponseEntity.ok(success.feed().posts());
        }

        return ResponseEntity.internalServerError().build();
    }
}
