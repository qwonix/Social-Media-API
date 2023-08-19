package ru.qwonix.test.social.media.api.serivce;

import ru.qwonix.test.social.media.api.entity.Image;

import java.util.Optional;


public interface ImageService {

    Image save(Image image);

    Boolean existsByName(String name);

    Optional<Image> findByName(String name);

    Boolean isImageOwner(String imageName, String username);
}
