package ru.qwonix.test.social.media.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import ru.qwonix.test.social.media.api.facade.ImageFacade;
import ru.qwonix.test.social.media.api.result.FindImageEntries;
import ru.qwonix.test.social.media.api.result.UploadImageEntries;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/image")
public class ImageController {

    private final ImageFacade imageFacade;

    @GetMapping("/{name}")
    public ResponseEntity<?> get(@PathVariable String name) {
        var result = imageFacade.findByName(name);

        if (result instanceof FindImageEntries.Result.NotFound) {
            return ResponseEntity.notFound().build();
        } else if (result instanceof FindImageEntries.Result.Success success) {
            return ResponseEntity.ok(success.imageResponseDto().resource());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PreAuthorize("hasAuthority('UPLOAD_IMAGE')")
    @PostMapping("/upload")
    public ResponseEntity<?> upload(UriComponentsBuilder uriComponentsBuilder, @RequestParam("image") MultipartFile image) {
        var result = imageFacade.upload(image);

        if (result instanceof UploadImageEntries.Result.Fail) {
            return ResponseEntity.badRequest().build();
        } else if (result instanceof UploadImageEntries.Result.Success success) {
            return ResponseEntity.created(
                    uriComponentsBuilder.path("/api/v1/image/{name}")
                            .build(Map.of("name", success.imageResponseDto().name()))
            ).build();
        }
        return ResponseEntity.internalServerError().build();
    }


}
