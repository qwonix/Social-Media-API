package ru.qwonix.test.social.media.api.serivce;

import ru.qwonix.test.social.media.api.entity.Image;


public interface ImageService {

    Image save(Image image);

    Boolean existsByName(String name);
}
