package ru.qwonix.test.social.media.api.serivce.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.qwonix.test.social.media.api.entity.Image;
import ru.qwonix.test.social.media.api.repository.ImageRepository;
import ru.qwonix.test.social.media.api.serivce.ImageService;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Boolean existsByName(String name) {
        return imageRepository.existsByImageName(name);

    }
}
