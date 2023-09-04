package ru.qwonix.test.social.media.api.facade;

import org.springframework.web.multipart.MultipartFile;
import ru.qwonix.test.social.media.api.result.FindImage;
import ru.qwonix.test.social.media.api.result.UploadImage;

public interface ImageFacade {
    FindImage.Result findByName(String name);

    UploadImage.Result upload(MultipartFile image, String username);
}
