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
    public ResponseEntity<PostResponse> getById(@PathVariable("id") UUID id) {
        log.debug("Get post with id {}", id);
        var result = postFacade.find(id);
        if (result instanceof FindPost.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof FindPost.Result.Success success) {
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
    public ResponseEntity<PostResponse> createNewPost(@AuthenticationPrincipal UserDetails userDetails,
                                                      UriComponentsBuilder uriComponentsBuilder,
                                                      @RequestBody @Valid CreatePostRequest createPostRequest) {
        log.debug("Post creation request title {}", createPostRequest.title());
        var result = postFacade.create(createPostRequest, userDetails.getUsername());
        if (result instanceof CreatePost.Result.Success success) {
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
    public ResponseEntity<PostResponse> updateById(@PathVariable("id") UUID id,
                                                   @RequestBody @Valid UpdatePostRequest updatePostRequest) {
        log.debug("Post update request id {}", id);
        var result = postFacade.update(id, updatePostRequest);
        if (result instanceof UpdatePost.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof UpdatePost.Result.Success success) {
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
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        log.debug("Post delete request id {}", id);
        var result = postFacade.delete(id);
        if (result instanceof DeletePost.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof DeletePost.Result.Success) {
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
        if (result instanceof AttachImageToPost.Result.PostNotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof AttachImageToPost.Result.ImageNotFound) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("imageName", "image does not exist. In order to attach an image, you need to upload it")
            );
        } else if (result instanceof AttachImageToPost.Result.ImageAlreadyAttached) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("imageName", "the image has already been attached to the post")
            );
        } else if (result instanceof AttachImageToPost.Result.Success success) {
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
        if (result instanceof DetachImageFromPost.Result.PostNotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof DetachImageFromPost.Result.ImageNotFound) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("imageName", "image does not exist. In order to detach an image, you need to upload and attach it")
            );
        } else if (result instanceof DetachImageFromPost.Result.ImageNotAttached) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("imageName", "the image is not attached to the post")
            );
        } else if (result instanceof DetachImageFromPost.Result.Success success) {
            return ResponseEntity.ok(success.postResponse());
        }

        return ResponseEntity.internalServerError().build();
    }
}
