package ru.qwonix.test.social.media.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import ru.qwonix.test.social.media.api.dto.ErrorResponse;
import ru.qwonix.test.social.media.api.dto.MessageResponse;
import ru.qwonix.test.social.media.api.dto.SendMessageRequest;
import ru.qwonix.test.social.media.api.facade.ChatFacade;
import ru.qwonix.test.social.media.api.result.FindChatEntries;
import ru.qwonix.test.social.media.api.result.SendMessageEntries;

import java.util.Optional;

@Tag(name = "Chat", description = "Message endpoints")
@SecurityRequirement(name = "Bearer")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/chat/{username}")
public class ChatController {

    private final ChatFacade chatFacade;

    @Operation(summary = "Send message to user", responses = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not a friend", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable String username,
                                         @RequestBody @Valid SendMessageRequest sendMessageRequest) {
        log.debug("User {} send message to user {}", userDetails.getUsername(), username);
        var result = chatFacade.sendMessage(userDetails.getUsername(), username, sendMessageRequest);

        if (result instanceof SendMessageEntries.Result.SenderNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("sender", "user doesn't exist"));
        } else if (result instanceof SendMessageEntries.Result.RecipientNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("recipient", "user doesn't exist"));
        } else if (result instanceof SendMessageEntries.Result.NonFriends) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("relation", "you have to be a friend to send a message"));
        } else if (result instanceof SendMessageEntries.Result.Success success) {
            return ResponseEntity.ok(success.message());
        }

        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Get chat messages with user (sorted by descending date – new first)", parameters = {
            @Parameter(name = "page", description = "Chat page"),
            @Parameter(name = "count", description = "Count of messages per page")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Chat messages retrieved successfully", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class)))
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping
    public ResponseEntity<?> getChat(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable String username,
                                     @RequestParam Optional<Integer> page,
                                     @RequestParam Optional<Integer> count
    ) {
        log.debug("User {} send message to user {}", userDetails.getUsername(), username);
        var result = chatFacade.findChatPaginated(userDetails.getUsername(), username, page.orElse(0), count.orElse(10));

        if (result instanceof FindChatEntries.Result.SenderNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("sender", "user doesn't exist"));
        } else if (result instanceof FindChatEntries.Result.RecipientNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("recipient", "user doesn't exist"));
        } else if (result instanceof FindChatEntries.Result.Success success) {
            return ResponseEntity.ok(success.messages());
        }

        return ResponseEntity.internalServerError().build();
    }
}
