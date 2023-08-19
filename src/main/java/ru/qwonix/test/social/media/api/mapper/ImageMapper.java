package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.util.UriComponentsBuilder;
import ru.qwonix.test.social.media.api.dto.ImageResourceResponseDto;
import ru.qwonix.test.social.media.api.dto.ImageResponseDto;
import ru.qwonix.test.social.media.api.entity.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "name", source = "image.imageName")
    ImageResponseDto map(Image image);

    ImageResourceResponseDto map(String name, Resource resource);

}
