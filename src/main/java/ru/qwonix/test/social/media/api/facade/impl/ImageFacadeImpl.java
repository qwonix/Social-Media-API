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
        if (Boolean.TRUE.equals(imageService.existsByName(name))) {
            Resource resource;
            try {
                resource = storageService.loadAsResource(name);
            } catch (MalformedURLException e) {
                return FindImageEntries.Result.NotFound.INSTANCE;
            }
            var imageResponseDto = imageMapper.map(name, resource);
            return new FindImageEntries.Result.Success(imageResponseDto);
        } else {
            return FindImageEntries.Result.NotFound.INSTANCE;
        }
    }

    @Override
    public UploadImageEntries.Result upload(MultipartFile image, String username) {
        var optionalUserProfile = userProfileService.findUserByUsername(username);
        if (optionalUserProfile.isPresent()) {
            var user = optionalUserProfile.get();

            var filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            try {
                storageService.store(image, filename);
            } catch (IOException e) {
                return UploadImageEntries.Result.UserNotFound.INSTANCE;
            }
            var saved = imageService.save(new Image(filename, user));

            var imageResponse = imageMapper.map(saved);
            return new UploadImageEntries.Result.Success(imageResponse);
        } else {
            return UploadImageEntries.Result.UserNotFound.INSTANCE;
        }

    }
}
