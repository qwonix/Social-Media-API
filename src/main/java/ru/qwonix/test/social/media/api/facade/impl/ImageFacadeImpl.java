package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.qwonix.test.social.media.api.entity.Image;
import ru.qwonix.test.social.media.api.facade.ImageFacade;
import ru.qwonix.test.social.media.api.mapper.ImageMapper;
import ru.qwonix.test.social.media.api.result.FindImageEntries;
import ru.qwonix.test.social.media.api.result.UploadImageEntries;
import ru.qwonix.test.social.media.api.serivce.ImageService;
import ru.qwonix.test.social.media.api.serivce.StorageService;
import ru.qwonix.test.social.media.api.serivce.UserProfileService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ImageFacadeImpl implements ImageFacade {

    private final UserProfileService userProfileService;
    private final ImageService imageService;
    private final StorageService storageService;
    private final ImageMapper imageMapper;

    @Override
    public FindImageEntries.Result findByName(String name) {
        if (Boolean.FALSE.equals(imageService.existsByName(name))) {
            return FindImageEntries.Result.NotFound.INSTANCE;
        }

        Resource resource;
        try {
            resource = storageService.loadAsResource(name);
        } catch (MalformedURLException e) {
            return FindImageEntries.Result.NotFound.INSTANCE;
        }
        return new FindImageEntries.Result.Success(imageMapper.map(name, resource));
    }

    @Override
    public UploadImageEntries.Result upload(MultipartFile image, String username) {
        if (image.isEmpty() || image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            return UploadImageEntries.Result.FileIsNotImage.INSTANCE;
        }
        var optionalUserProfile = userProfileService.findUserByUsername(username);
        if (optionalUserProfile.isEmpty()) {
            return UploadImageEntries.Result.UserNotFound.INSTANCE;
        }

        var user = optionalUserProfile.get();

        var filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        try {
            storageService.store(image, filename);
        } catch (IOException e) {
            return UploadImageEntries.Result.UserNotFound.INSTANCE;
        }
        var saved = imageService.save(new Image(filename, user));

        return new UploadImageEntries.Result.Success(imageMapper.map(saved));
    }
}
