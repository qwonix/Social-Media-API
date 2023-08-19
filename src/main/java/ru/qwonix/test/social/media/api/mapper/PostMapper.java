package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.qwonix.test.social.media.api.dto.PostCreateDto;
import ru.qwonix.test.social.media.api.dto.PostResponseDto;
import ru.qwonix.test.social.media.api.dto.PostUpdateDto;
import ru.qwonix.test.social.media.api.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "title", source = "postUpdateDto.title")
    @Mapping(target = "text", source = "postUpdateDto.text")
    Post map(PostUpdateDto postUpdateDto);

    @Mapping(target = "title", source = "postCreateDto.title")
    @Mapping(target = "text", source = "postCreateDto.text")
    Post map(PostCreateDto postCreateDto);

    @Mapping(target = "owner", source = "post.user")
    PostResponseDto map(Post post);
}
