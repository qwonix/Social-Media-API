package ru.qwonix.test.social.media.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.qwonix.test.social.media.api.dto.PostCreateDto;
import ru.qwonix.test.social.media.api.dto.PostResponseDto;
import ru.qwonix.test.social.media.api.dto.PostUpdateDto;
import ru.qwonix.test.social.media.api.facade.PostFacade;
import ru.qwonix.test.social.media.api.facade.impl.AuthorizationFacadeImpl;
import ru.qwonix.test.social.media.api.result.CreatePostEntries;
import ru.qwonix.test.social.media.api.result.DeletePostEntries;
import ru.qwonix.test.social.media.api.result.FindPostEntries;
import ru.qwonix.test.social.media.api.result.UpdatePostEntries;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostFacade postFacade;
    private final AuthorizationFacadeImpl authorizationFacade;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> get(@PathVariable("id") UUID id) {
        log.debug("Get post with id {}", id);
        var result = postFacade.find(id);
        if (result instanceof FindPostEntries.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof FindPostEntries.Result.Success success) {
            return ResponseEntity.ok(success.postResponseDto());
        }

        return ResponseEntity.internalServerError().build();
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> create(@AuthenticationPrincipal UserDetails userDetails,
                                    UriComponentsBuilder uriComponentsBuilder,
                                    @RequestBody @Valid PostCreateDto postCreateDto) {
        log.debug("Post creation request title {}", postCreateDto.title());
        var result = postFacade.create(postCreateDto, userDetails.getUsername());
        if (result instanceof CreatePostEntries.Result.Success success) {
            return ResponseEntity.created(uriComponentsBuilder
                            .path("/api/v1/post/{postId}")
                            .build(Map.of("postId", success.postResponseDto().id())))
                    .body(success.postResponseDto());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PreAuthorize("@authorizationFacadeImpl.isPostOwner(#id, authentication.name)")
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponseDto> update(@PathVariable("id") UUID id,
                                                  @RequestBody @Valid PostUpdateDto postUpdateDto) {
        log.debug("Post update request id {}", id);
        var result = postFacade.update(id, postUpdateDto);
        if (result instanceof UpdatePostEntries.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof UpdatePostEntries.Result.Success success) {
            return ResponseEntity.ok(success.postResponseDto());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PreAuthorize("@authorizationFacadeImpl.isPostOwner(#id, authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        log.debug("Post delete request id {}", id);
        var result = postFacade.delete(id);
        if (result instanceof DeletePostEntries.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof DeletePostEntries.Result.Success) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }
}
