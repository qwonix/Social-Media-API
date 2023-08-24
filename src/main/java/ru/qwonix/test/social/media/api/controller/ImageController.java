package ru.qwonix.test.social.media.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import ru.qwonix.test.social.media.api.dto.ErrorResponse;
import ru.qwonix.test.social.media.api.facade.ImageFacade;
import ru.qwonix.test.social.media.api.result.FindImageEntries;
import ru.qwonix.test.social.media.api.result.UploadImageEntries;

import java.util.Map;

@Tag(name = "Image", description = "Image handling endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/image")
public class ImageController {

    private final ImageFacade imageFacade;

    @Operation(summary = "Get an image by name", responses = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully", content = {
                    @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            }),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    @GetMapping("/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        var result = imageFacade.findByName(name);

        if (result instanceof FindImageEntries.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof FindImageEntries.Result.Success success) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(success.imageResourceResponse().resource());
        }
        return ResponseEntity.internalServerError().build();
    }

    @Operation(summary = "Upload an image", responses = {
            @ApiResponse(responseCode = "201", description = "Image uploaded successfully and resource created"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    }, security = @SecurityRequirement(name = "Basic"))
    @PreAuthorize("hasAuthority('UPLOAD_IMAGE')")
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<?> uploadNewImage(@AuthenticationPrincipal UserDetails userDetails,
                                            UriComponentsBuilder uriComponentsBuilder,
                                            @RequestPart("image") MultipartFile image) {
        var result = imageFacade.upload(image, userDetails.getUsername());

        if (result instanceof UploadImageEntries.Result.UserNotFound) {
            return ResponseEntity.badRequest().build();
        } else if (result instanceof UploadImageEntries.Result.FileIsNotImage) {
            return ResponseEntity.badRequest().body(new ErrorResponse("image", "not a image"));
        } else if (result instanceof UploadImageEntries.Result.Success success) {
            return ResponseEntity.created(
                    uriComponentsBuilder.path("/api/v1/image/{name}")
                            .build(Map.of("name", success.imageResponse().imageName()))
            ).build();
        }
        return ResponseEntity.internalServerError().build();
    }


}
