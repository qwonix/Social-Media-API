package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.io.Resource;
import ru.qwonix.test.social.media.api.dto.ImageResponseDto;
import ru.qwonix.test.social.media.api.entity.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "name", source = "image.imageName")
    ImageResponseDto map(Image image, Resource resource);

    ImageResponseDto map(String name, Resource resource);

}
