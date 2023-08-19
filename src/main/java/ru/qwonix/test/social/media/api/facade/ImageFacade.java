package ru.qwonix.test.social.media.api.facade;

import org.springframework.web.multipart.MultipartFile;
import ru.qwonix.test.social.media.api.result.FindImageEntries;
import ru.qwonix.test.social.media.api.result.UploadImageEntries;

public interface ImageFacade {
    FindImageEntries.Result findByName(String name);

    UploadImageEntries.Result upload(MultipartFile image);
}
