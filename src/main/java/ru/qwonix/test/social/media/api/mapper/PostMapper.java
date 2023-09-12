package ru.qwonix.test.social.media.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.qwonix.test.social.media.api.dto.CreatePostRequest;
import ru.qwonix.test.social.media.api.dto.PostResponse;
import ru.qwonix.test.social.media.api.dto.UpdatePostRequest;
import ru.qwonix.test.social.media.api.entity.Post;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {

    Post map(UpdatePostRequest updatePostRequest);

    Post map(CreatePostRequest createPostRequest);

    @Mapping(target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "owner", source = "user")
    PostResponse map(Post post);
}
