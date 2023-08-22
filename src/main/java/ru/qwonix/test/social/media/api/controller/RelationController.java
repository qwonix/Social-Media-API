package ru.qwonix.test.social.media.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.qwonix.test.social.media.api.dto.ErrorResponse;
import ru.qwonix.test.social.media.api.facade.RelationFacade;
import ru.qwonix.test.social.media.api.result.AddFriendEntries;
import ru.qwonix.test.social.media.api.result.RemoveFriendEntries;

@Tag(name = "Relations", description = "Managing relations endpoint")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/v1/user/{username}/friend")
public class RelationController {

    private final RelationFacade relationFacade;

    @Operation(summary = "Send the friend request to another user", responses = {
            @ApiResponse(responseCode = "201", description = "Friend request sent successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "409", description = "Request already sent or users are already friends", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping
    public ResponseEntity<?> addFriend(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String username) {
        log.debug("User {} send friend request to user {}", userDetails.getUsername(), username);
        var result = relationFacade.addFriend(userDetails.getUsername(), username);
        if (result instanceof AddFriendEntries.Result.UserNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("username", "user doesn't exist"));
        } else if (result instanceof AddFriendEntries.Result.RepeatedRequest) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("username", "request has already been sent"));
        } else if (result instanceof AddFriendEntries.Result.UsersAreAlreadyFriends) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("username", "users are already friends"));
        } else if (result instanceof AddFriendEntries.Result.Success) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.internalServerError().build();
    }
    @Operation(summary = "Remove the friend from friend list", responses = {
            @ApiResponse(responseCode = "200", description = "Friend removed successfully"),
            @ApiResponse(responseCode = "400", description = "Users are not friends", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @DeleteMapping
    public ResponseEntity<?> removeFriend(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String username) {
        log.debug("User {} send unfriend request to user {}", userDetails.getUsername(), username);
        var result = relationFacade.removeFriend(userDetails.getUsername(), username);
        if (result instanceof RemoveFriendEntries.Result.UserNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("username", "user doesn't exist"));
        } else if (result instanceof RemoveFriendEntries.Result.UsersAreNotFriends) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("username", "users are not friends"));
        } else if (result instanceof RemoveFriendEntries.Result.Success) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }
}
