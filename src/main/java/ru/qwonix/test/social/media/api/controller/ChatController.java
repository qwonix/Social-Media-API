package ru.qwonix.test.social.media.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.qwonix.test.social.media.api.dto.ErrorResponse;
import ru.qwonix.test.social.media.api.dto.SendMessageDto;
import ru.qwonix.test.social.media.api.facade.ChatFacade;
import ru.qwonix.test.social.media.api.result.GetChatEntries;
import ru.qwonix.test.social.media.api.result.SendMessageEntries;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/chat/{username}")
public class ChatController {

    private final ChatFacade chatFacade;

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable String username,
                                         @RequestBody @Valid SendMessageDto sendMessageDto) {
        log.debug("User {} send message to user {}", userDetails.getUsername(), username);
        var result = chatFacade.sendMessage(userDetails.getUsername(), username, sendMessageDto);

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

    @GetMapping
    public ResponseEntity<?> getChat(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable String username,
                                     @RequestParam Optional<Integer> page,
                                     @RequestParam Optional<Integer> size
    ) {
        log.debug("User {} send message to user {}", userDetails.getUsername(), username);
        var result = chatFacade.getChatPaginated(userDetails.getUsername(), username, page.orElse(0), size.orElse(10));

        if (result instanceof GetChatEntries.Result.SenderNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("sender", "user doesn't exist"));
        } else if (result instanceof GetChatEntries.Result.RecipientNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("recipient", "user doesn't exist"));
        } else if (result instanceof GetChatEntries.Result.Success success) {
            return ResponseEntity.ok(success.messages());
        }

        return ResponseEntity.internalServerError().build();
    }


}
