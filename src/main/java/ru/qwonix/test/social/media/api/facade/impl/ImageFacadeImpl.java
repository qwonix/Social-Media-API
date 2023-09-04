package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.qwonix.test.social.media.api.entity.Image;
import ru.qwonix.test.social.media.api.facade.ImageFacade;
import ru.qwonix.test.social.media.api.mapper.ImageMapper;
import ru.qwonix.test.social.media.api.result.FindImage;
import ru.qwonix.test.social.media.api.result.UploadImage;
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
    public FindImage.Result findByName(String name) {
        if (!imageService.existsByName(name)) {
            return FindImage.Result.NotFound.INSTANCE;
        }

        Resource resource;
        try {
            resource = storageService.loadAsResource(name);
        } catch (MalformedURLException e) {
            return FindImage.Result.NotFound.INSTANCE;
        }
        return new FindImage.Result.Success(imageMapper.map(name, resource));
    }

    @Override
    public UploadImage.Result upload(MultipartFile image, String username) {
        if (image.isEmpty() || image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            return UploadImage.Result.FileIsNotImage.INSTANCE;
        }
        var optionalUserProfile = userProfileService.findUserByUsername(username);
        if (optionalUserProfile.isEmpty()) {
            return UploadImage.Result.UserNotFound.INSTANCE;
        }

        var filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        try {
            storageService.store(image, filename);
        } catch (IOException e) {
            return UploadImage.Result.UserNotFound.INSTANCE;
        }
        var saved = imageService.save(new Image(filename, optionalUserProfile.get()));

        return new UploadImage.Result.Success(imageMapper.map(saved));
    }
}
