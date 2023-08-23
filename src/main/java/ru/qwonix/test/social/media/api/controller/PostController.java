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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.qwonix.test.social.media.api.dto.*;
import ru.qwonix.test.social.media.api.facade.PostFacade;
import ru.qwonix.test.social.media.api.result.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Posts", description = "Posts endpoints")
@SecurityRequirement(name = "Bearer")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostFacade postFacade;

    @Operation(summary = "Get post", responses = {
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Post not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> get(@PathVariable("id") UUID id) {
        log.debug("Get post with id {}", id);
        var result = postFacade.find(id);
        if (result instanceof FindPostEntries.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof FindPostEntries.Result.Success success) {
            return ResponseEntity.ok(success.postResponse());
        }

        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Create new post", responses = {
            @ApiResponse(responseCode = "201", description = "Post created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            })
    })
    @PostMapping
    public ResponseEntity<PostResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                               UriComponentsBuilder uriComponentsBuilder,
                                               @RequestBody @Valid CreatePostRequest createPostRequest) {
        log.debug("Post creation request title {}", createPostRequest.title());
        var result = postFacade.create(createPostRequest, userDetails.getUsername());
        if (result instanceof CreatePostEntries.Result.Success success) {
            return ResponseEntity.created(uriComponentsBuilder
                            .path("/api/v1/post/{postId}")
                            .build(Map.of("postId", success.postResponse().id())))
                    .body(success.postResponse());
        }
        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Update post", responses = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Post not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PreAuthorize("@authorizationFacadeImpl.isPostOwnerOrIsPostNotFound(#id, authentication.name)")
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable("id") UUID id,
                                               @RequestBody @Valid UpdatePostRequest updatePostRequest) {
        log.debug("Post update request id {}", id);
        var result = postFacade.update(id, updatePostRequest);
        if (result instanceof UpdatePostEntries.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof UpdatePostEntries.Result.Success success) {
            return ResponseEntity.ok(success.postResponse());
        }
        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Delete post", responses = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Post not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PreAuthorize("@authorizationFacadeImpl.isPostOwnerOrIsPostNotFound(#id, authentication.name)")
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

    @Operation(summary = "Attach image to post", responses = {
            @ApiResponse(responseCode = "201", description = "Image attached successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Post not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Image not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Image already attached", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PreAuthorize("@authorizationFacadeImpl.isPostOwnerOrIsPostNotFound(#id, authentication.name) and " +
                  "@authorizationFacadeImpl.isImageOwnerOrIsImageNotFound(#attachedImage.imageName(), authentication.name)")
    @PostMapping("/{id}/image")
    public ResponseEntity<?> attachImage(UriComponentsBuilder uriComponentsBuilder,
                                         @PathVariable("id") UUID id,
                                         @RequestBody AttachImageRequest attachedImage) {
        log.debug("Attach image {} to post {}", id, attachedImage.imageName());
        var result = postFacade.attachImage(id, attachedImage);
        if (result instanceof AttachImageToPostEntries.Result.PostNotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof AttachImageToPostEntries.Result.ImageNotFound) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("imageName", "Image does not exist. In order to attach an image, you need to upload it")
            );
        } else if (result instanceof AttachImageToPostEntries.Result.ImageAlreadyAttached) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("imageName", "The image has already been attached to the post")
            );
        } else if (result instanceof AttachImageToPostEntries.Result.Success success) {
            return ResponseEntity.created(uriComponentsBuilder
                            .path("/api/v1/post/{postId}")
                            .build(Map.of("postId", success.postResponse().id())))
                    .body(success.postResponse());
        }

        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Detach image from post", responses = {
            @ApiResponse(responseCode = "201", description = "Image detached successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Post not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Image not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Image not attached", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PreAuthorize("@authorizationFacadeImpl.isPostOwnerOrIsPostNotFound(#id, authentication.name)")
    @DeleteMapping("/{id}/image")
    public ResponseEntity<?> detachImage(@PathVariable("id") UUID id, @RequestBody DetachImageRequest detachedImage) {
        log.debug("Detach image {} to post {}", id, detachedImage.imageName());
        var result = postFacade.detachImage(id, detachedImage);
        if (result instanceof DetachImageFromPostEntries.Result.PostNotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof DetachImageFromPostEntries.Result.ImageNotFound) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("imageName", "Image does not exist. In order to detach an image, you need to upload and attach it")
            );
        } else if (result instanceof DetachImageFromPostEntries.Result.ImageNotAttached) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("imageName", "The image is not attached to the post")
            );
        } else if (result instanceof DetachImageFromPostEntries.Result.Success success) {
            return ResponseEntity.ok(success.postResponse());
        }

        return ResponseEntity.internalServerError().build();
    }
}
