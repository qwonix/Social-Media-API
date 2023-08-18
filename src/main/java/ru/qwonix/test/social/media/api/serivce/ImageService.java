package ru.qwonix.test.social.media.api.serivce;

import org.springframework.web.multipart.MultipartFile;
import ru.qwonix.test.social.media.api.entity.Image;

import java.util.Optional;

public interface ImageService {
    Optional<Image> findByName(String name);

    Image uploadFile(MultipartFile image);
}
